import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class RunProj{
   int[][] zArr={1,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,1,1,0,1,0,0};
   public static void main( String[] args ) throws IOException  {
	   PrintWriter out = new PrintWriter(new FileWriter("output.txt"), true);
	   int cnt=1;
      for(double m=0.030;m<0.065;m=m+0.010)//aha-4 
      for(double l=0.6;l<1.5;l=l+0.2)//b - 5
      for(int i=0;i<4;i=i+1)//corpus - 4
         for(double k=0.0;k<0.11;k=k+0.02)//error - 6
         for(double j=0.6;j<2;j=j+0.2){//ratio - 7
            	System.out.println("**********************");
            	System.out.println("Starting run #"+ cnt);
            	System.out.println("**********************");
            	cnt++;
               double[][] input= {{1,0,j,0}};
               myModel myMod=new myModel(input, i, k, m, l);
               double Obj=myMod.getObj();
               double[] Z=myMod.getZ();    
               double lay=layout(Z);
               out.write(Arrays.toString(input[0]));
               out.write("  Corpus:"+i +"  Error:"+k +"  ratio:"+j +"  layout:"+lay+"  WPM:"+ (12*100000/Obj)); 
               out.write("\n");
         }

      for(int i=0; i<zArr.length; i=i+1){
         out.write("\n\n\n");
         out.write("Z"+(i+1)+":\n");
         out.write(Arrays.toString(zArr[i]));
      }

      out.close();
   }
   
   public static int layout(double[] input){

      //double[] z1={1,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,1,1,0,1,0,0};
      //double[] z2={1,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,1,1,1,1,0,0};
      //double[] z3={1,0,1,1,1,1,1,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,1,1,1,1,0,0};

      for(int i=0; i<zArr.length; i=i+1){
         if (Arrays.equals(input,zArr[i]))    
            return i+1;
      }

      zArr.add(input);
      return zArr.length;
      /*   
      if (Arrays.equals(input,z1)) 
         return 1;
      if (Arrays.equals(input,z2)) 
         return 2;
      if (Arrays.equals(input,z3)) 
         return 3;
      return 0;   
      */
   }
}