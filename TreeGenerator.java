import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FamilyTree {

    public FamilyTree(){
    }

    class node
    {
        String father;
        String mother;
        String name;
        int A,B;
        public node(){
            this.father=null;
            this.mother=null;
            this.name=null;
            A = B = 1000;
        }
        public node(String na){
            this.father=null;
            this.mother=null;
            this.name=na;
            A = B = 1000;
        }
        public node(String fa, String ma,String na){
            this.father=fa;
            this.mother=ma;
            this.name=na;
            A = B = 1000;
        }

    }
    public boolean searchnode(String name)
    {
        for(int i=0;i<members.size();i++)
        {
            if((members.get(i).name).equalsIgnoreCase(name))
            {return true;}
        }
        return false;
    }
    public int searchnodeplace(String name)
    {
        for(int i=0;i<members.size();i++)
        {
            if((members.get(i).name).equalsIgnoreCase(name))
            {return i;}
        }
        return -1;
    }


    ArrayList<node> members=new ArrayList<>();
    ArrayList<node> ancestors = new ArrayList<>();
    ArrayList<String> ancestorFinal = new ArrayList<>();
    ArrayList<Integer> ansdistance=new ArrayList<>();



    public void buildFamilyTree(String familyFile) throws Exception{

        BufferedReader r;
        try
        {
            r=new BufferedReader(new FileReader(familyFile));
            String line =r.readLine();
            while(line!=null)
            {
                String[] words=line.split("\\s");
                words=line.trim().split("\\s+");

                for(int i=0;i<words.length;i++)
                {
                    if(i==0) {
                        boolean jj = searchnode(words[i]);
                        if (jj) {}
                        else {
                            node f = new node(words[0]);
                            members.add(f);
                        }
                    }
                    else if(i==1) {
                        boolean jj=searchnode(words[i]);
                        if(jj){}
                        else{node m=new node(words[1]);
                            members.add(m);
                        }}
                    else if(i>1) {
                        boolean jj=searchnode(words[i]);
                        if(jj==true){
                            int no=searchnodeplace(words[i]);
                            members.get(no).father=words[0];
                            members.get(no).mother=words[1];
                        }
                        else
                        {
                            node o=new node(words[0],words[1],words[i]);
                            members.add(o);

                        }
                    }}

                line=r.readLine();
            }
            r.close();
        }
        catch (Exception e){e.printStackTrace();}
    }


    public void evaluate(String queryFile,String outputFile) throws Exception{
        /*
         * Traverse the tree to answer the queries
         * For information on queries take a look at the handout
         */
        BufferedReader r;
        BufferedWriter w;
        try
        {
            r=new BufferedReader(new FileReader(queryFile));
            String line =r.readLine();
            w=new BufferedWriter(new FileWriter(outputFile));
            w.write("");
            while(line!=null){


                String[] que=line.split("\\s");
                que=line.trim().split("\\s+");

                boolean descendant1 = checkfordescendent(que[0],que[1]);
                boolean descendand2 = checkfordescendent(que[1],que[0]);
                if (descendant1){
                    w.append(que[0]+" is a descendant of "+que[1]+"\n");
                }else if(descendand2){
                    w.append(que[1]+" is a descendant of "+que[0]+"\n");
                }else {
                    ancestors = new ArrayList();
                    ancestorFinal = new ArrayList<>();
                    checkforclose(que[0],que[1]);
                    if(ancestors.size()==0)
                        w.append("unrelated\n");
                    else {
                        int[] count = new int[ancestors.size()];
                        int c = 0;
                        for(node i: ancestors){
                            i.A = i.B = 1000;
                            count[c] = checkfordistance(que[0],i.name,0) ;
                            System.out.println("done first "+count[c]);
                                    count[c]+=checkfordistance(que[1],i.name,0);
                            System.out.println(count[c]);c++;
                        }
                        String g="";int kk=0;
                        int min = getMin(count);

                        for(kk=0;kk<ancestors.size();kk++){
                            if(count[kk]==min){
                                ancestorFinal.add(ancestors.get(kk).name);
                            }
                        }
                        Collections.sort(ancestorFinal);
                        for(kk=0;kk<ancestorFinal.size()-1;kk++)
                        {g=g+ancestorFinal.get(kk)+" ";}
                        g=g+ancestorFinal.get(kk);
                        w.append(g+"\n");
                    }
                }

                line=r.readLine();
            }r.close();w.close();

        }
        catch (Exception e){e.printStackTrace();}

    }

    public static int getMin(int[] inputArray){
        int minValue = inputArray[0];
        for(int i=1;i<inputArray.length;i++){
            if(inputArray[i] < minValue){
                minValue = inputArray[i];
            }
        }
        return minValue;
    }
    int couz=0;
    int cou=0;
    public void checkforclose(String n1, String n2){
        node temp1 = members.get(searchnodeplace(n1));
        node temp2 = members.get(searchnodeplace(n2));

        if(checkfordescendent(n2,n1))
        {
            ancestors.add(temp1);
            System.out.println(n2+"<>"+n1);
        }
        if(temp1.father!=null)
        {
            checkforclose(temp1.father, n2);
            checkforclose(temp1.mother, n2);
        }


    }
    public int checkfordistance(String n1,String n2,int countm)
    {
        int ip=searchnodeplace(n1);
        System.out.println(n1+" MMM "+n2);

        node temp = members.get(ip);

        if(temp.name.equals(n2)){
            return countm;
        }else {
            if(temp.father!=null)
            {int jk=checkfordistance(temp.father,n2,++countm);
                    int jl=checkfordistance(temp.mother,n2,countm);
                    if(jk>=jl){return jl;}
                    else {return jk; }}

            else{
                return 1000;}

        }
    }



    public boolean checkfordescendent(String n1,String n2)
    {
        int ip=searchnodeplace(n1);
        node temp = members.get(ip);
        if(temp.name.equals(n2)){
            return true;
        }else {
            if(temp.father!=null)
            { return checkfordescendent(temp.father,n2)||checkfordescendent(temp.mother,n2);}
            else return false;
        }
    }



   
}
