package wumpusworld;
public class MyAgent implements Agent
{
    private World w;
    int rnd;
    int[][] box = new int[4][4];
    int[][] visited = new int[4][4];
    int dir0=-1;
    int dir=-1,done=0;
    int count=0;
    int count1=0;
    int dir1=0;
    public MyAgent(World world)
    {
        w = world;   
    }
    public void direction(int x,int y) {
		int i=0;
		if (w.getDirection() == World.DIR_RIGHT) 
			i = 0;
		if (w.getDirection() == World.DIR_LEFT) 
			i = 2;
		if (w.getDirection() == World.DIR_UP) 
			i = 1;
		if (w.getDirection() == World.DIR_DOWN)
			i = 3;
		if ((i == 0 && x == 2) || (x == 0 && i == 2) || (i == 1 && x == 3) || (x == 1 && i == 3)) {
			w.doAction(World.A_TURN_RIGHT);
			w.doAction(World.A_TURN_RIGHT);
		}

		if ((i == 3 && x == 2) || (i == 2 && x == 1) || (i == 1 && x == 0) || (i == 0 && x == 3)) 
			w.doAction(World.A_TURN_RIGHT);

		if ((x == 3 && i == 2) || (x == 2 && i == 1) || (x == 1 && i == 0) || (x == 0 && i == 3)) 
			w.doAction(World.A_TURN_LEFT);	
		if(y==1)
			w.doAction(World.A_MOVE);
		else
			w.doAction(World.A_SHOOT);
	}
    public void safe(int x,int y)
    {   	
    	if(x>=0 && y>=0 && y<=3 && x<=3)
    	    box[x][y]=1;	 	
    }  
    public int safePath(int x,int y)
    {   
    	int min=1000;
    	if(x>=0 && y>=0 && y<=3 && x<=3)
    	{
    		if(visited[x][y]==0)
    		{
    		if(w.isVisited(x+1,y+1))
    		{
    			int[] a = new int[4];
    			visited[x][y]=1;
    			a[0]=box[x][y]+safePath(x+1,y);
    			a[1]=box[x][y]+safePath(x,y+1);
    			a[2]=box[x][y]+safePath(x-1,y);
    			a[3]=box[x][y]+safePath(x,y-1);		
    			 for(int i=0;i<4;i++)
    				 if(min>a[i])
    				 { dir0=i;
    				  min=a[i];
    				 }		 
    			 visited[x][y]=0;    
    			 return min;
    		}
    		else
    		{
    			return box[x][y];
    		}
    		}
    	}
    	return 10000;
    
    }      
    public void breezeAdjacentboxes(int x,int y) 
    {
    	if(x>=0 && y>=0 && y<=3 && x<=3)
    	{
    	if(!w.isVisited(x+1,y+1)) 
    	{
    		if(box[x][y]!=1 && box[x][y]!=165)
    		{
    			if(box[x][y]==130 || box[x][y]==170)
    				box[x][y]=60;
    			else
    				box[x][y]=box[x][y]+30;
    				
    		}
    	}
    	} 	
    } 
    public void breezeMethod(int x,int y)
    {
    	if(box[x][y]!=165)
    	  box[x][y]=1;
    	breezeAdjacentboxes(x+1,y);
    	breezeAdjacentboxes(x,y+1);
    	breezeAdjacentboxes(x-1,y);
    	breezeAdjacentboxes(x,y-1);	
    	safePath(x,y);
        direction(dir0,1);
    }
    public void strenchAdjacentboxes(int x,int y,int s,int a) 
    {
    	if(x>=0 && y>=0 && y<=3 && x<=3)
    	{
    	 if ( !w.isVisited(x+1,y+1) ) 
    	 {
    		
    		if(box[x][y]!=1 && box[x ][y] != 165)
    		{
    			if(!(box[x][y]%30==0 && box[x][y]>0) )
    			{
    				if(box[x][y]==130 || box[x][y]==170 || box[x][y]==185  )
    				{
    					count++;
    					dir=a;
    				}
    				else
    				{
    					if(s==1)
    						box[x][y]=130;
    					else
    						box[x][y]=170;
    				    count1++;
    				    dir1=a;			    
    				}
    			}
    		}
    	}
    	}
    	
    }
    public void shoot(int x,int y,int d)
    { 	
        
        direction(d,0);
    	for (int i = 0; i < 4; i++)
		for (int j = 0; j < 4; j++) 
		{
				if (box[i][j] == 130 )
					box[i][j] = 1;
				if (box[i][j] == 170)
					box[i][j] = 30;
		}
    }
    public void strenchmethod(int x,int y,int s)
    {   
    	count=0;
    	count1=0;
    	if(box[x][y]!=165)
    	 box[x][y]=1;
    	strenchAdjacentboxes(x+1,y,s,0);
    	strenchAdjacentboxes(x,y+1,s,1);
    	strenchAdjacentboxes(x-1,y,s,2);
    	strenchAdjacentboxes(x,y-1,s,3);
    	if(x==0 && y==0)
    	{
    		done=1;
    		direction(0,0);
    		if(w.hasStench(x+1,y+1))
    		{
    			box[0][1]=185;
    			direction(0,1);
    			if(w.hasBreeze(x+1,y+1))
    				box[1][0]=30;
    			else
    				box[1][0]=1;
    			
    			return;
    		}
    		else
    		{  
    			direction(0,1);
    			if(w.hasBreeze(x+1,y+1))
    			{
    				box[1][0]=30;
    				box[0][1]=30;
    			}
    			else
    			{
    				box[1][0]=1;
    				box[0][1]=1;
    			}
    			return;
    		}
    	}
    	
    	if(count==1 && done==0)
    	{
    		 shoot(x,y,dir);
    		 return;
    	}
    	else
    	{
    		if(count1==1)
    		{
    			shoot(x,y,dir1);
    		    return;}
    	}
    	safePath(x,y);  	
        direction(dir0,1);
    }
    public void doAction()
    {      
        int x = w.getPlayerX()-1;
        int y = w.getPlayerY()-1;                 
        if (w.hasGlitter(x+1,y+1)) 
        {
			w.doAction(World.A_GRAB);
			return;
		}
        if (w.hasStench(x+1,y+1) && w.isInPit()) 
        {
			if (w.hasGlitter(x+1, y+1)) 
			{
				w.doAction(World.A_GRAB);
				return;
			}
			box[x][y] = 165;
			w.doAction(World.A_CLIMB);
			strenchmethod(x,y,1);
			return;
		}
        if (w.hasBreeze(x+1, y+1) && w.isInPit() )
		{
			box[x][y]=165;
			if (w.hasGlitter(x+1, y+1)) {
				w.doAction(World.A_GRAB);
				return;
			}
			w.doAction(World.A_CLIMB);
			breezeMethod(x,y);
			return;
		}
        if (w.hasGlitter(x+1,y+1) && w.hasBreeze(x+1, y+1)) {
			w.doAction(World.A_GRAB);
			return;
		}
        if (w.hasBreeze(x+1,y+1) && w.hasStench(x+1,y+1)) {
			
        	strenchmethod(x,y,2);
        	return;
	      }
        if (w.hasBreeze(x+1,y+1) ) {
        	breezeMethod(x,y);
        	return;
		} 
        if (w.hasStench(x+1,y+1)) {
        	strenchmethod(x,y,1);
            return;
		}
        if (w.hasGlitter(x+1,y+1)) {
			w.doAction(World.A_GRAB);
			return;
		}
        if (w.isInPit()) {		
	       box[x][y] = 165;		   
			w.doAction(World.A_CLIMB);
			safePath(x,y);
	        direction(dir0,1);
			return;				
		}
        safe(x,y+1);
        safe(x,y-1);
        safe(x-1,y);
        safe(x+1,y);  
        safePath(x,y);
        box[x][y]=1;    
        direction(dir0,1);
    }    
}

