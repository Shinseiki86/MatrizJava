package matriz;


import java.util.Date;
import java.util.Random;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

public class PnlMatriz extends javax.swing.JPanel {
    private JTextPane [][] arrTxtFields;
    
    public static final boolean ENABLE = true;
    public static final boolean DISABLE = false;
    
    
    public JTextPane[][] getArrTxtFields() {
        return arrTxtFields;
    }
    
    public PnlMatriz(int tamanoMatriz, boolean estado) {
        initComponents();
        arrTxtFields = new JTextPane[tamanoMatriz][tamanoMatriz];
        this.setLayout(new java.awt.GridLayout(tamanoMatriz,tamanoMatriz));
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=0;j<tamanoMatriz;j++){
                arrTxtFields[i][j] = crearTextBox(estado);
                add(arrTxtFields[i][j]);
            }
        }
        this.repaint();
    }
    
    public PnlMatriz(boolean estado) {
        initComponents();
        int tamanoMatriz = 3;
        arrTxtFields = new JTextPane[tamanoMatriz][tamanoMatriz];
        setLayout(new java.awt.GridLayout(tamanoMatriz,tamanoMatriz));
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=0;j<tamanoMatriz;j++){
                arrTxtFields[i][j] = crearTextBox(estado);
                add(arrTxtFields[i][j]);
            }
        }
        this.repaint();
    }
    
    
    private JTextPane crearTextBox(boolean estado) {
        JTextPane txtBox = new JTextPane();
        
        //Tamaño de fuente calculado dependiendo del tamaño del arreglo
        txtBox.setFont(new java.awt.Font("Tahoma", 0, 110/arrTxtFields.length));
        
        //Listener que suprime el TAB en los JTextPane, permitiendo navegar entre ellos.
        txtBox.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_TAB){
                    if (e.getModifiersEx() == KeyEvent.SHIFT_DOWN_MASK ) {
                        txtBox.transferFocusBackward();
                    } else {
                        txtBox.transferFocus();
                    }
                    e.consume();
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        txtBox.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
                txtBox.selectAll();
            }
            @Override
            public void focusLost(FocusEvent e) {
                txtBox.setText("\r\n"+ txtBox.getText().trim());
            }
        });
        
        if(!estado)
            txtBox.setFocusable(estado);
        
        
        txtBox.setBorder(BorderFactory.createEtchedBorder());
        
            StyledDocument doc = txtBox.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            
        return txtBox;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(300, 300));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 272, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void matrizInversa(PnlMatriz pnlMatrizOrg) {
        double[][] matriz = pnlMatrizOrg.getArreglo();
        
        System.out.println("Matriz " + matriz.length+"x"+matriz.length);
        Date fchIni = new Date();
        System.out.println("Inicia en: " + fchIni);

//        System.out.println("Matriz Original:");
//        imprimirMatriz(matriz);
        
        System.out.println("Calculando determinante... ");
        double det=determinante(matriz);
        if (det!=0){
            
            System.out.println("Determinante: " + det);
            
            System.out.println("calculando matriz Adjunta...");
            double[][] mAdj=matrizAdjunta(matriz);
//            imprimirMatriz(nmatriz);
            
            System.out.println("Calculando matriz inversa...");
            Fraccion invMatriz[][] = invMatriz(det,mAdj);
//            imprimirMatriz(invMatriz);
            
            llenar(invMatriz);
            
            Date fchFin = new Date();
            System.out.println("Termina en: " + fchFin);

            java.util.Calendar calIni = java.util.Calendar.getInstance();
            calIni.setTime(fchIni);
            java.util.Calendar calFin = java.util.Calendar.getInstance();
            calFin.setTime(fchFin);

            double diferencia = calFin.getTimeInMillis()- calIni.getTimeInMillis() ;
            System.out.println("Segundos transcurridos: " + String.format( "%.4f", diferencia/1000 ));
            System.out.println("+++++++++++++++++++++++++++");
            //imprimirMatriz(nmatriz);
            
        } else {
            JOptionPane.showMessageDialog(null, "Determinante de matriz es cero (0).", "No existe Inv", JOptionPane.ERROR_MESSAGE);
            System.out.println("Determinante de matriz es 0.");
        }
    }
    
    public double determinante(double[][] matriz){
        if(matriz.length==2){
            double det=(matriz[0][0]*matriz[1][1])-(matriz[1][0]*matriz[0][1]);
            return det;
        }
        double suma=0;
        for(int i=0; i<matriz.length; i++){
            double[][] nm=new double[matriz.length-1][matriz.length-1];
            for(int j=0; j<matriz.length; j++){
                if(j!=i){
                    for(int k=1; k<matriz.length; k++){
                        int indice=-1;
                        if(j<i)
                            indice=j;
                        else if(j>i)
                            indice=j-1;
                        nm[indice][k-1]=matriz[j][k];
                    }
                }
            }
            if(i%2==0)
                suma+=matriz[i][0] * determinante(nm);
            else
                suma-=matriz[i][0] * determinante(nm);
        }
        return suma;
    }
    
    public double[][] matrizAdjunta(double [][] matriz){
        System.out.println("\tCalculando matriz de Cofactores...");
        double mCofactores[][] = matrizCofactores(matriz);
        System.out.println("\tCalculando matriz Transpuesta a matriz de cofactores...");
        return matrizTranspuesta(mCofactores);
    }
    
    public double[][] matrizCofactores(double[][] matriz){
        double[][] nm=new double[matriz.length][matriz.length];
        for(int i=0;i<matriz.length;i++) {
            for(int j=0;j<matriz.length;j++) {
                double[][] det=new double[matriz.length-1][matriz.length-1];
                double detValor;
                for(int k=0;k<matriz.length;k++) {
                    if(k!=i) {
                        for(int l=0;l<matriz.length;l++) {
                            if(l!=j) {
                                int indice1=k<i ? k : k-1 ;
                                int indice2=l<j ? l : l-1 ;
                                det[indice1][indice2]=matriz[k][l];
                            }
                        }
                    }
                }
                detValor=determinante(det);
                nm[i][j]=detValor * (double)Math.pow(-1, i+j+2);
                System.out.println("Cofactor ["+i+"]["+j+"]");
            }
        }
        return nm;
    }
    
    public double[][] matrizTranspuesta(double [][] matriz){
        double[][]nuevam=new double[matriz[0].length][matriz.length];
        for(int i=0; i<matriz.length; i++){
            for(int j=0; j<matriz.length; j++)
                nuevam[i][j]=matriz[j][i];
        }
        return nuevam;
    }
    
    public Fraccion [][] invMatriz(double det, double[][] matriz) {
        //multiplicar 1/det por la matriz Adjunta
        double n = det;
        Fraccion [][] matrizStr = new Fraccion[matriz.length][matriz.length];
        for(int i=0;i<matriz.length;i++){
            for(int j=0;j<matriz.length;j++){
                matrizStr[i][j]=new Fraccion(matriz[i][j]+"/"+n);
            }
        }
        return matrizStr;
    }
    
    public double [][] getArreglo(){
        int tamanoMatriz = arrTxtFields.length;
        double[][] matriz = new double[tamanoMatriz][tamanoMatriz];
        
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=tamanoMatriz-1;j>=0;j--){
                try{
                    matriz[i][j] = Double.parseDouble(arrTxtFields[i][j].getText().replace("\n", ""));
                }catch(NumberFormatException ex){
//                    JOptionPane.showMessageDialog(null, "Valor "+arrTxtFields[i][j].getText()+ " no valido.\n"
//                            + "Ingrese solo números enteros", "Error!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        //imprimirMatriz(matriz);
        return matriz;
    }
    
    public void llenar() {
        int tamanoMatriz = arrTxtFields.length;
        Random rnd = new Random();
        
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=0;j<tamanoMatriz;j++){
                if(arrTxtFields[i][j].getText().trim().isEmpty()){
                    double num = (int)(rnd.nextDouble() * 10);
                    String txtNum = String.format( "%.0f", num );
                    //String txtNum = "" + num ;
                    arrTxtFields[i][j].setText("\r\n"+txtNum);
                }
                //arrTxtFields[i][j].requestFocus();
            }
        }
        repaint();
    }    
    
    public void llenar(double num) {
        int tamanoMatriz = arrTxtFields.length;
        
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=0;j<tamanoMatriz;j++){
                String txtNum = String.format( "%.0f", num );
                try{
                    int n = Integer.parseInt(arrTxtFields[i][j].getText().trim());
                    if(arrTxtFields[i][j].getText().trim().isEmpty()){
                        arrTxtFields[i][j].setText("\r\n"+txtNum);
                    }
                    
                } catch (NumberFormatException ex){
                    arrTxtFields[i][j].setText("\r\n"+0);
                }
                
            }
        }
        repaint();
    }
    
    public void llenar(double[][] matriz) {
        int tamanoMatriz = arrTxtFields.length;
        
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=tamanoMatriz-1;j>=0;j--){
                try{
                    String txtNum = (new Fraccion(new java.math.BigDecimal(matriz[i][j]))).toString();
                    arrTxtFields[i][j].setText("\r\n"+txtNum);
                }catch(NumberFormatException err){
                    arrTxtFields[i][j].setText("\r\n"+err.getMessage());
                }
            }
        }
        repaint();
    }
    
    public void llenar(Fraccion[][] matriz) {
        int tamanoMatriz = arrTxtFields.length;
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=tamanoMatriz-1;j>=0;j--){
                try{
                    String txtNum = matriz[i][j].toString();
                    arrTxtFields[i][j].setToolTipText(txtNum);
                    arrTxtFields[i][j].setText(txtNum);
                }catch(NumberFormatException err){
                    arrTxtFields[i][j].setText("\r\n"+err.getMessage());
                }
            }
        }
        repaint();
    }
    
    public void reset() {
        int tamanoMatriz = arrTxtFields.length;
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=0;j<tamanoMatriz;j++){
                arrTxtFields[i][j].setText("\r\n0");
                arrTxtFields[i][j].setText("\r\n");
            }
        }
        repaint();
    }
    
    private void imprimirMatriz(double [][] matriz){
        int tamanoMatriz = matriz.length;
        for(int i=0;i<tamanoMatriz;i++){
            for(int j=0; j<tamanoMatriz;j++){
                System.out.print("("+i+","+j+") "+matriz[i][j]+"\t");
            }
            System.out.println("");
        }
    }
}