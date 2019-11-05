
package lavanderia;

import java.awt.Color;
import static java.lang.Thread.sleep;
import java.text.DecimalFormat;
import javax.swing.*;

public class Lavanderia implements Runnable {
    JButton Start; //Button Start, Change Text to Stop
    //Var for clock
    JTextField CronoM, CronoH; //Shows timer 
    int contadorM = 0;
    int contadorH = 0;//Set time in 0
    int contadorT = 0;
    int InputTimeIntM =0;
    int InputTimeIntH = 0; 
    //Interfaces Objects
    JTextField InputTimeM, InputTimeH, CLS,CSS,CTS;
    JButton AddLavadora,AddSecadora,DeleteLavadora,DeleteSecadora;
    JButton L1,L2,L3,L4,L5; //Icono lavadoras
     JButton S1,S2,S3,S4,S5; //Icono secadoras
    int NLavadoras, NSecadoras;
    String cadena = "";
    String cadena1 = "";
    int auxto;
    int minTS;
    int minTSL ;
    int minTSS ;
    //EXP
      int Exp;
      int temp;
      double x = 17;
      double a = 5;
      double mod = 991;
      double lambdaTLL = 3; // Lambda (valor variable)
      //Var
      int TLL; //Tiempo de Llegada (Exp)
      int TServicio; //Tiempo de Servicio
      int TSTL;      //Tiempo de Servicio total de lavadoras
      int TSTS;      //Tiempo de servicio total de secadoras 
      int TSL[];//Tiempo de servicio de las lavadoras
      int TSS[];  //Tiempo de servicio de las secadoras
      int TOL[];  //Tiempo de ocio de las lavadoras;
      int TOS[];  //Tiempo de ocio de las secadoras;
      int ColaL=0;    //Cola lavadoras
      int ColaS=0;    //Cola secadoras
      int TET;      //Tiempo de espera total
      int TETL;     //Tiempo de espera total lavadoras;
      int TETS;     //Tiempo de espera total secadoras
      int TOTL=0;      //Tiempo de ocio total lavadoras
      int TOTS=0;       //Tiempo de ocio total secadoras
      int NCLL=0;   //Numero de clientes llegados
      int NCAL=0;    //Numero de cargas lavadas  
      int NCAS=0;   //Numero de cargas secadas
      int DELTA;    
      int TLIMITE;  //Tiempo Limite
      
    public Lavanderia(JButton Start, JTextField CronoM, JTextField CronoH, JTextField InputTimeM ,JTextField InputTimeH,
            JButton AddLavadora, JButton AddSecadora,JButton DeleteLavadora,JButton DeleteSecadora,
            int NLavadoras,int NSecadoras, JTextField CLS, JTextField CSS,JButton L1,JButton L2,JButton L3,JButton L4,JButton L5,
            JButton S1,JButton S2,JButton S3,JButton S4,JButton S5, JTextField CTS){
    this.Start = Start;
    this.CronoM = CronoM;
    this.CronoH = CronoH;
    this.InputTimeM = InputTimeM;
    this.InputTimeH = InputTimeH;
    this.AddLavadora = AddLavadora;
    this.AddSecadora = AddSecadora;
    this.DeleteLavadora = DeleteLavadora;
    this.DeleteSecadora = DeleteSecadora;
    this.NLavadoras = NLavadoras;
    this.NSecadoras = NSecadoras;
    this.CLS = CLS;  
    this.CSS = CSS;
    this.L1 = L1;
    this.L2 = L2;
    this.L3 = L3;
    this.L4 = L4;
    this.L5 = L5;
    this.S1 = S1;
    this.S2 = S2;
    this.S3 = S3;
    this.S4 = S4;
    this.S5 = S5;
    this.CTS = CTS;
    TSL= new int[NLavadoras];
    TSS = new int[NSecadoras];
    TOL= new int [NLavadoras];
    TOS= new int[NSecadoras];
    Exponencial();
    TLL = Exp;
    InputTimeIntM = Integer.parseInt(InputTimeM.getText()); //Get Time
    InputTimeIntH = Integer.parseInt(InputTimeH.getText());
    TLIMITE = InputTimeIntM + (InputTimeIntH*60);

  
    }

    @Override
    public void run() {
        //Mientras el reloj no pase el tiempo limite
        IconStart();
        while(contadorT<TLIMITE){
            Disable();
            System.out.println("*****************************************************************");
            Delta();  //Actualiza Delta
            TETL= TETL + ColaL*DELTA;
            TETS = TETS + ColaS*DELTA;
            Clock();

            TLL = TLL - DELTA;
            
            if(TLL==0){
                NCLL ++; //Llega un cliente
                ColaL ++; //Se agrega una carga de ropa a la cola de la lavadora
                Exponencial();
                TLL = Exp;
                CLS.setText(""+ColaL);
            }     
            //Recorre las lavadoras
            int i =0;
          while(i<NLavadoras){
              
            System.out.println("******************LAVADORA NUMERO "+i+"******");   
            TSL[i]-=DELTA;
            System.out.println("TSL-Delta "+TSL[i]);

            //Revision del Tiempo de servicio
            //Tiempo de Servicio = 0
            if((TSL[i]==0)|| TSL[i]<0){
                if(TSL[i]==0){
             System.out.println("***************TERMINO DE LAVAR************");
             LDesocupada(i);
             ColaS ++;
             CSS.setText(""+ColaS);
             NCAL++;
                }
             //Tiempo de Ocio
                else if(TSL[i]<0){
            TOL[i] -= TSL[i];
            auxto = TOL[i];
            TOTL += TOL[i];
            TSL[i]=0;
            }
                //Revisa la cola
            if(ColaL>0){
                      System.out.println("LAVADORA "+i+"COMIENZA A TRABAJAR");
                      ColaL--;
                     LOcupada(i);
                     
                      ServicioExp();
                      TSL[i]=temp;
                      CLS.setText(" "+ColaL);
   
                }
                     else if(ColaL<0){
                      LDesocupada(i);
                      }
            
                
            }
            //Aun no termina
            else if(TSL[i]>0){
                System.out.println("LAVADORA "+i+"SIGUE OCUPADA");
            }

            i++;   //Seguimos con la siguiente lavadora 
          }
          //Fin de recorrido de las lavadoras
          
          
          //Inicio Secadoras
           int j =0;
          while(j<NSecadoras){
                         
            System.out.println("******************SECADORA NUMERO "+j+"******");
            
            TSS[j]-=DELTA;
                        System.out.println("TSS-Delta "+TSS[j]);

            //Revision del Tiempo de servicio
            //Tiempo de Servicio = 0
            if((TSS[j]==0)|| TSS[j]<0){
                if(TSS[j]==0){
             System.out.println("***************TERMINO DE SECAR************");
             SDesocupada(j);
             NCAS ++;
             CTS.setText(""+NCAS);
             
                }
             //Tiempo de Ocio
                else if(TSS[j]<0){
            TOS[j] -= TSS[j];
            auxto = TOS[j];
            TOTS += TOS[j];
            TSS[j]=0;
            }
            if(ColaS>0){
                      System.out.println("SECADORA "+j+"COMIENZA A TRABAJAR");
                      ColaS--;
                     SOcupada(j);
                     
                      ServicioExp();
                      TSS[j]=temp;
                      CSS.setText(" "+ColaS);
   
                }
                     else if(ColaS<0){
                      LDesocupada(j);
                      }
            
                
            }
            //Aun no termina
            else if(TSS[j]>0){
                System.out.println("SECADORA "+i+"SIGUE OCUPADA");
            }

            j++;
          }
          //Fin de recorrido de las lavadoras
      
         try {
                sleep(2000); //1000 = 1 seg
            } catch (Exception e) {
                System.out.println("Exception");
            }
       
    }
        //Ends Simulation, Return Values to Objects 
        System.out.println("Fin de la simulacion");
        System.out.println("Cargas lavadas"+NCAL);
        TOImpresion();
      
        Enabled();

    }
      //Thread
      public void start() {
        new Thread(this).start();
    }
      //Se genera la variable de tiempo de llegada con distribucion exponencial
      public void Exponencial(){
      
        double R = (a*x)%mod; // Creacion del numero aleatorio
        x = R;
        R = R/mod;
        
        double X = -(Math.log(R)/lambdaTLL)*100; // Funcion de distribucion exponencial
        double VarX =Math.round(X);
        Exp = (int)(VarX);
        String SVarX = String.valueOf(Exp);
        System.out.println("----------TIEMPO DE LLEGADA: " + SVarX);      
    }
      //Se genera la variable de tiempo de servicio con distribucion exponencial
      public void ServicioExp(){
   
        int max = 150;
        int min = 30;
        int verificacion = 0;
         
        do{
        double R = (a*x)%mod; // Creacion del numero aleatorio
        x = R;
        R = R/mod;
        double lambda = 3.5; // Lambda (valor variable)
        double X = -(Math.log(R)/lambda); // Funcion de distribucion exponencial
        temp = (int)(X*100);
        if (temp > min && temp < max){
            System.out.println("Tiempo de servicio exponencial: " + temp);      
            verificacion = 1;
        }
        else{
            verificacion = 0;
        }
        }while(verificacion == 0);
        
        
    
}
      //Deshabilita los botones y campos de textos al iniciar la simulacion
      public void Disable(){
          //Block objects while running
          InputTimeM.setEditable(false);  
         InputTimeM.setBackground(Color.gray);
          InputTimeH.setEditable(false);  
         InputTimeH.setBackground(Color.gray);
         AddLavadora.setEnabled(false);
         AddSecadora.setEnabled(false);
         DeleteLavadora.setEnabled(false);
         DeleteSecadora.setEnabled(false);
          Start.setEnabled(false);
         

      }
      //Habilita los botones y campos de texto
      public void Enabled(){
        
        InputTimeM.setEditable(true);
        InputTimeM.setBackground(Color.white);
         InputTimeH.setEditable(true);  
         InputTimeH.setBackground(Color.white);
        AddLavadora.setEnabled(true);
        AddSecadora.setEnabled(true);
        DeleteLavadora.setEnabled(true);
        DeleteSecadora.setEnabled(true);
        Start.setEnabled(true);

      }
      //Actualiza el reloj en horas y minutos
      public void Clock(){
          int aux = 0;
       contadorM+= DELTA; //Add DELTA Selected by the minor of three generators
                    
            if(contadorM==60){
             contadorM = 0;
             contadorH ++;
             }
              if(contadorM>60){
              aux = contadorM/60;
              contadorH+=aux;
              contadorM-=(aux*60);
              }     
         
            //Set 0 in left
            if(contadorM<10){
              CronoM.setText("0"+contadorM);
            }
            else{
              CronoM.setText(""+contadorM);
            }
            if(contadorH<10){
              CronoH.setText("0"+contadorH);
            }
            else{
              CronoH.setText(""+contadorH);
            }
            
      
      }
      //Se cambia el color de la lavadora si esta ocupada
      public void LOcupada(int i){
      switch(i){
          case 0: 
              L1.setBackground(Color.red);
              break;
          case 1: 
              L2.setBackground(Color.red);
              break;
          case 2: 
              L3.setBackground(Color.red);
              break;
          case 3: 
              L4.setBackground(Color.red);
              break;
          case 4: 
              L5.setBackground(Color.red);
              break;
         
      }
      
      }
      //Se cambia el color de la lavadora si esta desocupada
      public void LDesocupada(int i){
      switch(i){
          case 0: 
              L1.setBackground(Color.green);
              break;
          case 1: 
              L2.setBackground(Color.green);
              break;
          case 2: 
              L3.setBackground(Color.green);
              break;
          case 3: 
              L4.setBackground(Color.green);
              break;
          case 4: 
              L5.setBackground(Color.green);
              break;
      }
      
      }
       //Se cambia el color de la lavadora si esta ocupada
      public void SOcupada(int i){
      switch(i){
          case 0: 
              S1.setBackground(Color.red);
              break;
          case 1: 
              S2.setBackground(Color.red);
              break;
          case 2: 
              S3.setBackground(Color.red);
              break;
          case 3: 
              S4.setBackground(Color.red);
              break;
          case 4: 
              S5.setBackground(Color.red);
              break;
         
      }
      
      }
       //Se cambia el color de la secadora si esta desocupada
      public void SDesocupada(int i){
      switch(i){
          case 0: 
              S1.setBackground(Color.green);
              break;
          case 1: 
              S2.setBackground(Color.green);
              break;
          case 2: 
              S3.setBackground(Color.green);
              break;
          case 3: 
              S4.setBackground(Color.green);
              break;
          case 4: 
              S5.setBackground(Color.green);
              break;
      }
      
      }
      //Se inicializan los iconos en color verde
      public void IconStart(){
        L1.setBackground(Color.green);
        L2.setBackground(Color.green);
        L3.setBackground(Color.green);
        L4.setBackground(Color.green);
        L5.setBackground(Color.green);
        S1.setBackground(Color.green);
        S2.setBackground(Color.green);
        S3.setBackground(Color.green);
        S4.setBackground(Color.green);
        S5.setBackground(Color.green);
        CSS.setText(""+0);
        CLS.setText(""+0);
        TETL = 0;
        TETS = 0;
        TOTL = 0;
        TOTS = 0;
        NCLL = 0;
        NCAL = 0;
        NCAS = 0;
      } 
      //Actualiza delta seleccionando el minimo mayor que 0
      public void Delta(){  
            menor();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!DELTAAAAAAAAA!!!!!!!!!!!");
            if(minTSL==0){
            DELTA = TLL;
            }
            else if((TLL<minTSL)&&(TLL<minTSS)){
            DELTA = TLL;
            }
            else if((TLL<minTSL)&&(minTSS==0)){
                DELTA=TLL;
            }
            else if((TLL<minTSS)&&(minTSL==0)){
                DELTA=TLL;
            
            }
          
            else if((minTSL<TLL&&minTSL<minTSS)||(minTSL<TLL&&minTSS==0)){
            DELTA = minTSL;
            }
            else if(((minTSS<TLL&&minTSS<minTSL)||(minTSS<TLL&&minTSL==0))&&minTSS!=0){
            DELTA = minTSS;
            }
           
            System.out.println("****DELTA"+DELTA);
            contadorT += DELTA;
            System.out.println("Contador T : "+contadorT);
                  
      }
      //Tiempo de ocio
      public void TOImpresion(){
          TOTL =0;
          TOTS =0;
          
          for(int i=0; i<NLavadoras; i++){
          TOTL += TOL[i];
        }
          for(int i=0; i<NSecadoras; i++){
          TOTS += TOS[i];
        }        
        JOptionPane.showMessageDialog(null , "Tiempo de Ocio Total de las Lavadoras " + TOTL +  " minutos \n" + 
                "Tiempo de Ocio Total de las Secadoras " + TOTS + " minutos");
          
        for(int i=0; i<NLavadoras; i++){
          System.out.println("Tiempo de Ocio Lavadora "+(i+1)+": "+ TOL[i]);
          cadena = cadena + "Tiempo de ocio lavadora " + i +   "\n" + TOL[i] + " minutos\n";
        }
           for(int i=0; i<NSecadoras; i++){
          System.out.println("Tiempo de Ocio Secadora "+(i+1)+": "+ TOS[i]);
          cadena1 = cadena1 + "Tiempo de ocio secadora " + i  + "\n" + TOS[i] + " minutos\n";
        }
        JOptionPane.showMessageDialog(null , cadena + "\n" + 
                 cadena1);
       System.out.println("TETL"+TETL);
          System.out.println("TETS"+TETS);
          double DTETL = new Double(TETL);
          double DTETS = new Double(TETS);
          double DTLIMITE = new Double(TLIMITE);
          double TEML = DTETL/NCAL;
          double TEMS = DTETS/NCAS;
          System.out.println("Tiempo de espera medio en lavadoras = "+ TEML);
          System.out.println("Tiempo de espera medio en secadoras = "+TEMS);
        JOptionPane.showMessageDialog(null ,"Tiempo de espera medio en lavadoras = "+ TEML + " minutos. \n Tiempo de espera medio en secadoras = "+TEMS + "minutos\n" 
        + "Tiempo de espera total de lavadoras = " + TETL + "\nTiempo de espera total de secadoras = " + TETS + "\nTiempo limite " + TLIMITE);
                
        
      }
      public void menor(){
            minTSL = TSL[0];
            minTSS = TSS[0];
            System.out.println("TLL " +TLL);
            for(int i=0; i<NLavadoras; i++){
                
                System.out.println("TSL "+i+" "+TSL[i]);
                if(minTSL==0){
                minTSL=TSL[i];
                }
                else if((TSL[i]>0)&&(TSL[i]<minTSL)){
                minTSL = TSL[i];
                }
            }    
            for(int j=0; j<NSecadoras; j++){
                System.out.println("TSS "+j+" "+TSS[j]);
                 if(minTSS==0){
                minTSS=TSS[j];
                }
             if((TSS[j]>0)&&(TSS[j]<minTSS)){
                minTSS = TSS[j];
                }
            
            }
          System.out.println("MINTSL"+minTSL);
          System.out.println("MINTSS"+minTSS);
      
      
      }
     
    
}
