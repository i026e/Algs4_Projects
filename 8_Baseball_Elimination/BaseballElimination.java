import java.util.HashMap;
import java.util.Arrays;
//import java.util.Collections;

public class BaseballElimination
{
    private final int numTeams;
    
    private HashMap<Integer, Bag<Integer>> certificates;
    
    private String[] teamsArray;
    private int[] loss;
    private int[] remain;
    private int[] win;
    
    private int[][] games;
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename)
    {
        In in = new In(filename);
        this.numTeams = in.readInt();        
        
        certificates = new HashMap<Integer, Bag<Integer>>(numTeams);
        
        teamsArray = new String[numTeams];
        loss = new int[numTeams];
        remain = new int[numTeams];
        win = new int[numTeams];
        games = new int[numTeams][numTeams];
        
        
        for (int i = 0; i < numTeams; i++)
        {
            // String[] line = in.readLine().trim().split(" ");
            // StdOut.println(in.readString());
            teamsArray[i] = in.readString();
            win[i] = in.readInt();           
            loss[i] = in.readInt();
            remain[i] = in.readInt();
            for (int j = 0; j < numTeams; j++)
            {
                games[i][j] = in.readInt();
            }
        }
        
    }
    
    // number of teams
    public int numberOfTeams()
    {
        return this.numTeams;
    }
    // all teams
    public Iterable<String> teams()
    {
        return Arrays.asList(teamsArray);
    }
    private int teamNumber(String team)
    {
        for (int i = 0; i < numTeams; i++)
        {
            if (teamsArray[i].equals(team))
                return i;
        }
        throw new java.lang.IllegalArgumentException();
    }
    // number of wins for given team
    public int wins(String team)
    {
        return this.win[teamNumber(team)];
    }
    // number of losses for given team
    public int losses(String team) 
    {
        return this.loss[teamNumber(team)];
    }
    // number of remaining games for given team
    public int remaining(String team)
    {
        return this.remain[teamNumber(team)];
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2)    
    {
        return this.games[teamNumber(team1)][teamNumber(team2)];
    }
    
    private FlowNetwork createFlowNetwork(int teamEx)
    {
        // Number of games between all teams, no games with itself
        // sum (1 .. #teams - 1) = (#teams -1 +1)*(#teams - 1)/2
        int numGames = numTeams*(numTeams-1)/2;
        // # verticies: s, t, #teams, #games
        int numVerticies = 2 + numTeams + numGames;
        
        int s = numVerticies-2;
        int t = numVerticies-1;
        
        int exPossibleWins = win[teamEx] + remain[teamEx];
        FlowNetwork fn = new FlowNetwork(numVerticies);
        //add edges from team to t
        for (int i = 0; i < numTeams; i++)
        {
            if (i != teamEx)
               fn.addEdge(new FlowEdge(i, t, exPossibleWins - win[i]));
        }
        // add edges from s to games and from games to teams
        for (int i = 0, g = 0; i < numTeams; i++)
        {
            for (int j = i + 1; j < numTeams; j++, g++)
            {
                if (i != teamEx && j != teamEx)
                {
                    fn.addEdge(new FlowEdge(s, numTeams+g, games[i][j]));
                    fn.addEdge(new FlowEdge(numTeams+g, i, Double.MAX_VALUE));
                    fn.addEdge(new FlowEdge(numTeams+g, j, Double.MAX_VALUE));
                }
            }
                
        }
        //StdOut.println(fn);  
        
        return fn;
    }
    
    private FordFulkerson createFordFulkerson(FlowNetwork fn)
    {
        int t = fn.V()-1;
        int s = t-1;
        return new FordFulkerson(fn, s, t);
    }
    
    private boolean isFull(FlowNetwork fn)
    {
        int s = fn.V() - 2;
        for (FlowEdge e : fn.adj(s))
        {
            if (e.capacity() != e.flow())
                return false;
        }
        return true;
    }
    
    private boolean trivialCheck(int team)
    {
        int maxPossibleWin = win[team]+remain[team];
        for (int i = 0; i < numTeams; i++)
        {
            if (win[i] > maxPossibleWin)
            {
                Bag<Integer> b = new Bag<Integer>();
                b.add(i);
                certificates.put(team, b);
                return true;                
            }
        }
        return false;
    }
    private void doElimination(int team) 
    {       
        if (trivialCheck(team))
            return;
        
        FlowNetwork fn = createFlowNetwork(team);
        FordFulkerson maxflow = createFordFulkerson(fn);
        Bag<Integer> b = new Bag<Integer>();
        if (!isFull(fn))        
        {
            for (int v = 0; v < numTeams; v++)
            {
                if (maxflow.inCut(v))
                    b.add(v);
            }            
        }
        certificates.put(team, b);

    }
        
    // is given team eliminated?
    public boolean isEliminated(String team)              
    {
        int x = teamNumber(team);
        if (!certificates.containsKey(x))
            doElimination(x);
        
        return !certificates.get(x).isEmpty();
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team)  
    {
        int x = teamNumber(team);
        if (!certificates.containsKey(x))
            doElimination(x);
        if (certificates.get(x).isEmpty())
            return null;
        Bag<String> crt = new Bag<String>();
        //certificates.get(x)
        for (int i : certificates.get(x))
        {
            crt.add(teamsArray[i]);
        }
        return crt;
    }
    
    public static void main(String[] args) 
    {
    BaseballElimination division = new BaseballElimination(args[0]);
    for (String team : division.teams()) {
        if (division.isEliminated(team)) {
            StdOut.print(team + " is eliminated by the subset R = { ");
            for (String t : division.certificateOfElimination(team))
                StdOut.print(t + " ");
            StdOut.println("}");
        }
        else {
            StdOut.println(team + " is not eliminated");
        }
    }
}
}