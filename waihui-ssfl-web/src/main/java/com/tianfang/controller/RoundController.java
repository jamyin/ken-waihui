package com.tianfang.controller;

import com.tianfang.common.constants.DataStatus;
import com.tianfang.common.model.PageQuery;
import com.tianfang.common.model.PageResult;
import com.tianfang.common.model.Response;
import com.tianfang.dto.ScoreboardData;
import com.tianfang.train.dto.*;
import com.tianfang.train.service.*;
import com.tianfang.util.SSFLDatas;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据中心-赛事赛果
 *
 * @Author wangxiang
 * @Date 2016/3/18 16:25
 */
@Controller
@RequestMapping(value = "round")
public class RoundController extends BaseController {
    protected static Log logger = LogFactory.getLog(RoundController.class);
    private static String compId = SSFLDatas.compId;    // 上海业余足球联赛id
    private static int PAGESIZE = 5;    // 分页展示条数
    private static int SCORE_SIZE = 10; // 积分榜展示条数

    @Autowired
    private ICompetitionRoundService roundService;
    @Autowired
    private ICompetitionMatchService matchService;
    @Autowired
    private ICompetitionTeamService cTeamService;
    @Autowired
    private IMatchDatasService matchDatasService;
    @Autowired
    private ITeamPlayerService playerService;

    /**
     * 数据中心-赛事赛果首页
     *
     * @return
     */
    @RequestMapping(value = "index")
    public ModelAndView index() {
        ModelAndView mv = getModelAndView();
        // 获取所有的轮次
        List<CompetitionRoundDto> rounds = findAllRound();

        // 积分榜信息
        List<ScoreboardData> scoreboard = getScoreboard();

        mv.addObject("rounds", rounds);
        mv.addObject("scoreboard", scoreboard);
        mv.setViewName("/round/data-schedule");
        return mv;
    }

    /**
     * ajax 查询每轮下的比赛记录
     *
     * @param currRound 当前轮数页码
     * @param currPage
     * @return
     */
    @RequestMapping(value = "queryMatchs")
    @ResponseBody
    public Response<RoundDetail> queryMatchs(int currRound, int currPage){
        Response<RoundDetail> response = new Response<RoundDetail>();
        CompetitionRoundDto param = new CompetitionRoundDto();
        param.setCompId(compId);
        PageQuery query = new PageQuery();
        query.setCurrPage(currRound);
        query.setPageSize(1);
        PageResult<CompetitionRoundDto> round = roundService.findRoundByParam(param, query);
        if (null != round){
            if (null !=round.getResults() && round.getResults().size() > 0){
                try {
                    PageResult<CompetitionMatchDto> matchs = findMathByPage(round.getResults().get(0).getId(), currPage);
                    RoundDetail detail = new RoundDetail();
                    detail.setCurrRound(currRound);
                    if (round.getCurrPage() > 1){
                        detail.setUp(round.getCurrPage() - 1);
                    }
                    if (round.getCurrPage() < round.getTotalPage()){
                        detail.setDown(round.getCurrPage() + 1);
                    }
                    detail.setMatchs(matchs);
                    response.setData(detail);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                    response.setStatus(DataStatus.HTTP_FAILE);
                }
            }
        }

            return response;
    }

    /**
     * 根据比赛id查询详细信息
     * @param matchId
     * @param index 第几轮
     * @return
     */
    @RequestMapping(value = "detail")
    public ModelAndView detail(String matchId, Integer index){
        ModelAndView mv = getModelAndView();
        // 比赛信息
        CompetitionMatchDto match = matchService.getMatchById(matchId);
        // 组装比赛球队基本数据
        assemblyVSTeam(mv, match);
        // 组装时间轴数据
        assemblyVSHots(mv, match);
        // 组装球员数据
        assemblyVSPlayerDatas(mv,match);

        mv.addObject("index", index);
        mv.addObject("match", match);
        mv.setViewName("/round/data-team");
        return mv;
    }

    /**
     * 组装比赛球队基本数据
     * @param mv
     * @param match
     */
    private void assemblyVSTeam(ModelAndView mv, CompetitionMatchDto match) {
        if (null != match){
            List<MatchTeamBaseDatasDto> teamDatas = matchDatasService.queryTeamDatasByMatchId(match.getId());
            if (null != teamDatas && teamDatas.size() > 0){
                for (MatchTeamBaseDatasDto data : teamDatas){
                    if (data.getTeamId().equals(match.getHomeTeamId())){
                        mv.addObject("homeTeamData", data);
                    }
                    if (data.getTeamId().equals(match.getVisitingTeamId())){
                        mv.addObject("vsTeamData",data);
                    }
                }
            }
        }
    }

    /**
     * 根据赛事id查询比赛球员数据
     * @param matchId
     * @return
     */
    private List<MatchPlayerBaseDatasTempDto> getMatchPlayerBaseDatasTempDtos(String matchId) {
        MatchPlayerBaseDatasDto param = new MatchPlayerBaseDatasDto();
        param.setMatchId(matchId);
        List<MatchPlayerBaseDatasTempDto> matchPlayerBaseDatasTempDtos = matchDatasService.queryPlayerBaseDatasTempByParams(param);
        return matchPlayerBaseDatasTempDtos;
    }

    /**
     * 组装比赛球员主/客场球员数据
     * @param mv
     * @param match
     */
    private void assemblyVSPlayerDatas(ModelAndView mv, CompetitionMatchDto match) {
        if (null != match){
            List<MatchPlayerBaseDatasTempDto> matchPlayerBaseDatasTempDtos = getMatchPlayerBaseDatasTempDtos(match.getId());
            if (null != matchPlayerBaseDatasTempDtos && matchPlayerBaseDatasTempDtos.size() > 0){
                List<MatchPlayerBaseDatasTempDto> homePlayerDatas = new ArrayList<>();
                List<MatchPlayerBaseDatasTempDto> vsPlayerDatas = new ArrayList<>();
                for (MatchPlayerBaseDatasTempDto h : matchPlayerBaseDatasTempDtos){
                    if (h.getTeamId().equals(match.getHomeTeamId())){
                        homePlayerDatas.add(h);
                    }
                    if (h.getTeamId().equals(match.getVisitingTeamId())){
                        vsPlayerDatas.add(h);
                    }
                }
                mv.addObject("homePlayerDatas",homePlayerDatas);
                mv.addObject("vsPlayerDatas",vsPlayerDatas);
            }
        }
    }

    /**
     * 组装时间轴数据为主/客队
     * @param mv
     * @param match
     */
    private void assemblyVSHots(ModelAndView mv, CompetitionMatchDto match) {
        if (null != match){
            List<MatchPlayerHotDatasTempDto> hotdatas = getMatchPlayerHotDatasTempDtos(match.getId());
            if (null != hotdatas && hotdatas.size() > 0){
                List<MatchPlayerHotDatasTempDto> homeHots = new ArrayList<>();
                List<MatchPlayerHotDatasTempDto> vsHots = new ArrayList<>();
                for (MatchPlayerHotDatasTempDto h : hotdatas){
                    if (h.getTeamId().equals(match.getHomeTeamId())){
                        homeHots.add(h);
                    }
                    if (h.getTeamId().equals(match.getVisitingTeamId())){
                        vsHots.add(h);
                    }
                }
                mv.addObject("homeHots",homeHots);
                mv.addObject("vsHots",vsHots);
            }
        }
    }

    /**
     * 根据比赛id查询时间轴数据
     * @param matchId
     * @return
     */
    private List<MatchPlayerHotDatasTempDto> getMatchPlayerHotDatasTempDtos(String matchId) {
        MatchPlayerHotDatasDto param = new MatchPlayerHotDatasDto();
        param.setMatchId(matchId);
        return matchDatasService.queryPlayerHotDatasTempByParams(param);
    }

    /**
     * 获取积分榜数据
     *
     * @return
     */
    private List<ScoreboardData> getScoreboard() {
        CompetitionTeamDto param = new CompetitionTeamDto();
        param.setCompId(compId);
        PageQuery query = new PageQuery();
        query.setCurrPage(1);
        query.setPageSize(SCORE_SIZE);
        PageResult<CompetitionTeamDto> cTeams = cTeamService.findCompetitionTeamByParam(param, query);
        if (null != cTeams && null != cTeams.getResults() && cTeams.getResults().size() > 0) {
            List<ScoreboardData> list = new ArrayList<ScoreboardData>(cTeams.getResults().size());
            ScoreboardData data;
            CompetitionTeamDto team;
            for (int i = 0, len = cTeams.getResults().size(); i < len; i++) {
                team = cTeams.getResults().get(i);
                data = new ScoreboardData(i+1, team.getId(), team.getTeamName(), team.getTeamIcon(), team.getWin() == null ? 0 : team.getWin(),
                        team.getLose() == null ? 0 : team.getLose(), team.getDraw() == null ? 0 : team.getDraw());
                list.add(data);
            }
            return list;
        }

        return null;
    }

    /**
     * 分页查询该轮下的比赛信息
     *
     * @param roundId
     * @param currPage
     * @return
     */
    private PageResult<CompetitionMatchDto> findMathByPage(String roundId, int currPage) {
        CompetitionMatchDto param = new CompetitionMatchDto();
        param.setCompId(compId);
        param.setCroundId(roundId);
        PageQuery query = new PageQuery();
        query.setCurrPage(currPage);
        query.setPageSize(PAGESIZE);
        PageResult<CompetitionMatchDto> matchs = matchService.findCompetitionMatchViewByPage(param, query);

        return matchs;
    }

    /**
     * 查询所有场次
     *
     * @return
     */
    private List<CompetitionRoundDto> findAllRound(){
        CompetitionRoundDto param = new CompetitionRoundDto();
        param.setCompId(SSFLDatas.compId);
        return roundService.findRoundByParam(param);
    }
}

/**
 * 每轮下比赛记录数据封装
 */
class RoundDetail implements Serializable{

    @Getter
    @Setter
    private Integer currRound;  // 当前轮数据

    @Getter
    @Setter
    private Integer up; // 上一轮

    @Getter
    @Setter
    private Integer down;   // 下一轮

    @Getter
    @Setter
    private PageResult<CompetitionMatchDto> matchs; // 当前轮下比赛记录
}