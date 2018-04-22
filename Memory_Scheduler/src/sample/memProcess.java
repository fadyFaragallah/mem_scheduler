package sample;

import java.util.ArrayList;

public class memProcess {
    private String processId;
    private int processStart;
    private int processSize;


    public memProcess(String processId,int processStart,int processSize)
    {
       this.processId=processId;
        this.processStart=processStart;
        this.processSize=processSize;
    }

    public void setProcessId(String processId)
    {
        this.processId=processId;
    }
    public void setProcessStart(int processStart)
    {
        this.processStart=processStart;
    }
    public void setProcessSize(int processSize)
    {
        this.processSize=processSize;
    }
    public String getProcessId()
    {
        return this.processId;
    }
    public int getProcessStart()
    {
        return this.processStart;
    }
    public int getProcessSize()
    {
        return this.processSize;
    }
    public static void sort_start(ArrayList<memProcess>p,int sorting_method) //0: ascending  1: descending
    {
        for(int i=0;i<p.size()-1;i++)
        {
            for(int j=i+1;j<p.size();j++)
            {
                if(((p.get(j).getProcessStart()<p.get(i).getProcessStart()) &&(sorting_method==0))||((p.get(j).getProcessStart()>p.get(i).getProcessStart())&&(sorting_method==1)))
                {
                    // swapping
                    memProcess x=new memProcess(p.get(i).processId,p.get(i).processStart,p.get(i).processSize);
                    memProcess y=new memProcess(p.get(j).processId,p.get(j).processStart,p.get(j).processSize);
                    p.set(i,y);
                    p.set(j,x);
                }
            }
        }
        //printing
        for(int i=0;i<p.size();i++)
        {
            System.out.println(p.get(i).getProcessId()+" --- "+p.get(i).getProcessStart());
        }
    }

    public static void sort_size(ArrayList<memProcess>p,int sorting_method) //0: ascending  1: descending
    {
        for(int i=0;i<p.size()-1;i++)
        {
            for(int j=i+1;j<p.size();j++)
            {
                if(((p.get(j).getProcessSize()<p.get(i).getProcessSize()) &&(sorting_method==0))||((p.get(j).getProcessSize()>p.get(i).getProcessSize())&&(sorting_method==1)))
                {
                    // swapping
                    memProcess x=new memProcess(p.get(i).processId,p.get(i).processStart,p.get(i).processSize);
                    memProcess y=new memProcess(p.get(j).processId,p.get(j).processStart,p.get(j).processSize);
                    p.set(i,y);
                    p.set(j,x);
                }
            }
        }
    }

}
