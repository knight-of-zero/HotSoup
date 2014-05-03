package web;

import com.google.common.collect.Maps;
import table.HotSoup;
import table.Player;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Servlet for handling incoming requests.
 *
 * Created by dbemiller on 12/27/13.
 */
public class HotSoupServlet extends HttpServlet {

    /**
     * All the Players which are actively playing or watching the game.
     */
    private static final Map<String, Player> players = Maps.newConcurrentMap();

    /**
     * Handle the get requests coming in through the URL.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException
    {
        request.getSession();

        HotSoup game = HotSoup.getActiveGame();
        request.setAttribute("seatedPlayers", game.getSeatedPlayers());
        request.setAttribute("tricksSoFar", game.trickSoFar());
        request.setAttribute("numCardsRemaining", game.getRemainingCardsMap());

        request.setAttribute("nsScore", game.scoreNS());
        request.setAttribute("ewScore", game.scoreEW());

        RequestDispatcher dispatcher = request.getRequestDispatcher("soupInit.jsp");
        dispatcher.forward(request, response);
        response.getWriter().write("The return value here.");
    }

    /**
     * Find the Player associated with this session, if they're active at the table.
     * Otherwise return null, because they're either a spectator, or the game hasn't
     * started yet.
     */
    private static Player findPlayer(HttpSession session) {
        return players.get(session.getId());
    }
}
