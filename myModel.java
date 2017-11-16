import ilog.concert.*;
import ilog.cplex.*;

public class myModel {
     
   static double _BigM=-100;
   //static double =0.030;
   static double _MTrepeat=0.083;
   static double _a=0.083;
   double[] result=new double[28];   
   double totCost;
   
   IloNumVar[][] cvar = new IloNumVar[28][];
   IloNumVar[][] zvar = new IloNumVar[28][];
   
   public myModel(double[][] input, int corpusType, double err, double _ahadv, double bPerc){
      double _b=0.127*bPerc;
      paramCreater myPar=new paramCreater(input, corpusType);
      double[][] _A=myPar.getA();
      double[][] _A1=myPar.getA1();
      double[][] _freq=myPar.getFreq(corpusType);
      
      try {   

   	   IloCplex cplex = new IloCplex();
         double[] xlb =new double[28];
         double[] xub = new double[28];
         
         for(int i=0;i<28;i++){
            xlb[i] = 0.0;
            xub[i] = Double.MAX_VALUE;
            
         }                     
         
         for(int i=0;i<28;i++){
            IloNumVar[]     x  = cplex.boolVarArray(2);
            zvar[i] = x;
            IloNumVar[]     y  = cplex.numVarArray(28, xlb, xub);
            cvar[i] = y;    
           
         }      
         
         
         // Objective: minimize the sum of costs
         IloNumExpr y  = cplex.numVar(0.0, Double.MAX_VALUE);
         for (int i=0;i<28;i++){             
             y=cplex.sum(y,cplex.scalProd(cvar[i],_freq[i])); 
             
          }
         cplex.addMinimize(y);
       
         for (int j =  0; j < 28; j++) 
            for (int l=0;l<2;l++){
               double _rside=_MTrepeat + 2*err*(_a+_b*_A[j][27])+2*_BigM ;               
               cplex.addGe(cplex.sum(cplex.prod(_BigM,cplex.sum(zvar[27][l],zvar[j][l])),cvar[j][j]), _rside);
               
            }

         for (int j =  0; j < 28; j++) 
            for (int l=0;l<2;l++)
               for(int r=0;r<2;r++)
                  if(r!=l){
                     double _rside=_MTrepeat + err*(_a+_b*_A1[27][r]+_a+_b*_A1[j][l]-2*_ahadv)+2*_BigM  ;               
                     cplex.addGe(cplex.sum(cplex.prod(_BigM,cplex.sum(zvar[j][l],zvar[27][r])),cvar[j][j]), _rside);
                  }
      
         for (int i =  0; i < 28; i++)
            for (int j =  0; j < 28; j++)
               if(i!=j)
                  for (int l=0;l<2;l++){         
                     double _rside=_a+_b*_A[i][j] + 2*err*(_a+_b*_A[j][27])+3*_BigM ;               
                     cplex.addGe(cplex.sum(cplex.prod(_BigM,cplex.sum(zvar[i][l],zvar[j][l],zvar[27][l])),cvar[i][j]), _rside);
                     
                  }

         for (int i =  0; i < 28; i++) 
            for (int j =  0; j < 28; j++) 
               if(i!=j)
                  for (int l=0;l<2;l++) 
                     for (int r=0;r<2;r++)
                        if(r!=l){       
                           double _rside=_a+_b*_A[i][j] + err*(_a+_b*_A1[27][r]+_a+_b*_A1[j][l]-2*_ahadv)+3*_BigM;               
                           cplex.addGe(cplex.sum(cplex.prod(_BigM,cplex.sum(zvar[i][l],zvar[j][l],zvar[27][r])),cvar[i][j]), _rside);
                        }

         for (int i =  0; i < 28; i++)
            for (int j =  0; j < 28; j++)
               if(i!=j)
                  for (int l=0;l<2;l++) 
                     for (int r=0;r<2;r++)
                        if(r!=l){       
                           double _rside=_a+_b*_A1[j][l]-_ahadv+ 2*err*(_a+_b*_A[27][j])+3*_BigM;               
                           cplex.addGe(cplex.sum(cplex.prod(_BigM,cplex.sum(zvar[i][r],zvar[j][l],zvar[27][l])),cvar[i][j]), _rside);
                        }
         

         for (int i =  0; i < 28; i++)
            for (int j =  0; j < 28; j++)
               if(i!=j)
                  for (int l=0;l<2;l++) 
                     for (int r=0;r<2;r++)
                        if(r!=l){       
                           double _rside=_a+_b*_A1[j][l]-_ahadv + err*(_a+_b*_A1[27][r]+_a+_b*_A1[j][l]-2*_ahadv)+3*_BigM;               
                           cplex.addGe(cplex.sum(cplex.prod(_BigM,cplex.sum(zvar[i][r],zvar[j][l],zvar[27][r])),cvar[i][j]), _rside);
                        }     
       
         for (int i =  0; i < 28; i++) {
            cplex.addEq(cplex.sum(zvar[i]), 1);  
         }
         
         for (int l=0;l<2;l++){
            IloNumExpr x  = cplex.numVar(0.0, Double.MAX_VALUE);
            x=cplex.sum(zvar[0][l], 0);
            for (int i =  1; i < 28; i++) {
               x=cplex.sum(zvar[i][l], x);
            }
            cplex.addGe(x, 1);
            
         }
       
         if ( cplex.solve() ) {
             for(int i = 0; i < 28; i++)    	  
                result[i]=cplex.getValue(zvar[i][0]);   
            totCost=cplex.getObjValue();
           }
         
         cplex.end();
      
      }
      catch (IloException exc) {
         System.err.println("Concert exception '" + exc + "' caught");
      }
   }
   
   public double[] getZ(){
	   return result;
   }

   public double getObj(){
	   return totCost;
}
}