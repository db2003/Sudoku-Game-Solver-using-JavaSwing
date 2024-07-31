import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.swing.*;


public class Sudoku extends JFrame implements ActionListener, KeyListener {
    public static void main(String[] args) {
        new Sudoku();
    }
    private String[] Question = {};
    private ArrayList<String> questions = new ArrayList<>();
   
    private void loadPuzzlesFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() == 81) { // Check if the line represents a valid puzzle
                    questions.add(line);
                }
            }
            // Convert the ArrayList to an array
            Question = questions.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    int flag=1;
    int qm[][]=new int[9][10];
    Random random = new Random();
    int tar[][]=new int[9][10];
    int I=0,J=0;
    JButton bt[][] = new JButton[9][10];
    String test;

    //PANELS
    JPanel bgPanel=new JPanel();
    JPanel buttonPanel=new JPanel();
    JPanel gridPanel=new JPanel();


    //Buttons
    JButton newGameButton=new JButton("New Game");
    JButton solveButton=new JButton("Solve");
    JButton solveCellButton=new JButton("Solve Cell");
    JButton clearButton=new JButton("Clear");
    JButton restartButton=new JButton("Restart");
    Sudoku(){
        loadPuzzlesFromFile("sudoku_puzzles.txt");
        //POSITIONING OF BUTTONS IN BUTTON PANE
        newGameButton.setBounds(70,50,100,50);
        solveButton.setBounds(70,150,100,50);
        solveCellButton.setBounds(70,250,100,50);
        clearButton.setBounds(70,350,100,50);
        restartButton.setBounds(70,450,100,50);

        //ADDING BUTTONS IN BUTTON PANE
        buttonPanel.add(newGameButton);
        buttonPanel.add(solveButton);
        buttonPanel.add(solveCellButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(restartButton);

        //SETTINGS OF BUTTON
        solveButton.addActionListener(e -> get2Darray());
        clearButton.addActionListener(e -> clearBoard());
        solveCellButton.addActionListener(e -> solveASingleCell());
        restartButton.addActionListener(e -> clearGame());

        //SETTINGS OF PANEL
        buttonPanel.setOpaque(true);
        buttonPanel.setLayout(null);
        buttonPanel.setBackground(new Color(0xFFFFFFFF, true));
        buttonPanel.setBounds(750,0,250,600);

        bgPanel.setOpaque(true);
        bgPanel.setLayout(null);
        bgPanel.setBackground(new Color(0xFFFFFFFF, true));
        bgPanel.setBounds(0,0,750,600);

        gridPanel.setOpaque(true);
        gridPanel.setBackground(new Color(0xFF000000, true));
        gridPanel.setBounds(120,45,500,500);
        gridPanel.setLayout(new GridLayout(9, 9));

        newGameButton.addActionListener(this);

        //button designing
        for (int i = 0; i < 9; i++)
            for (int j = 0; j < 9; j++) {
                bt[i][j] = new JButton();
                bt[i][j].addActionListener(this);
                bt[i][j].addKeyListener(this);
                bt[i][j].setActionCommand(i + " " + j);
                bt[i][j].setBackground(Color.white);
                bt[i][j].setFont(new Font("UTM Micra", 1, 30));
                bt[i][j].setForeground(Color.black);
                gridPanel.add(bt[i][j]);
            }
        for (int i = 0; i < 9; i += 3)
            for (int j = 0; j < 9; j += 3) {
                bt[i][j].setBorder(BorderFactory.createMatteBorder(3,3,1,1, Color.black));
                bt[i][j + 2].setBorder(BorderFactory.createMatteBorder(3,1,1,3, Color.black));
                bt[i + 2][j + 2].setBorder(BorderFactory.createMatteBorder(1,1,3,3, Color.black));
                bt[i + 2][j].setBorder(BorderFactory.createMatteBorder(1,3,3,1, Color.black));
                bt[i][j + 1].setBorder(BorderFactory.createMatteBorder(3,1,1,1, Color.black));
                bt[i + 1][j + 2].setBorder(BorderFactory.createMatteBorder(1,1,1,3, Color.black));
                bt[i + 2][j + 1].setBorder(BorderFactory.createMatteBorder(1,1,3,1, Color.black));
                bt[i + 1][j].setBorder(BorderFactory.createMatteBorder(1,3,1,1, Color.black));
                bt[i + 1][j + 1].setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black));
            }


//ADDING AND FORMATTING FRAME
        //bgPanel.add(time_lb);
        bgPanel.add(gridPanel);
        this.add(bgPanel);
        this.add(buttonPanel);
        this.setSize(1000,640);
        this.setResizable(false);
        this.setLayout(null);
        this.setDefaultCloseOperation(3);
        this.setVisible(true);
    }

    private void clearGame() {
        putQuestionSudoku(test);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bgColorReset();

        if (e.getSource()==newGameButton) {
            flag=1;
            //newgame=1;
            textColorReset();
            int rand=random.nextInt(3875);
            test=Question[rand];
            putQuestionSudoku(test);
            solveBoard(tar);
        }

        else{
            String s = e.getActionCommand();
            int k = s.indexOf(32);
            int i = Integer.parseInt(s.substring(0, k));
            int j = Integer.parseInt(s.substring(k + 1, s.length()));
            I = i;
            J = j;

            //changing bg colors of same number
            sameNumbers(i,j);
        }
    }

    private void putQuestionSudoku(String test) {
        int temp=0;
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                int i1 = Integer.parseInt(test.charAt(temp) + "");
                if (i1 >0){
                    bt[i][j].setText(test.charAt(temp)+"");
                    qm[i][j]= i1;
                }
                else{
                    bt[i][j].setText("");
                    qm[i][j]=0;
                }
                temp++;
            }
        }
    }

    private void sameNumbers(int i,int j) {

        if(!Objects.equals(bt[i][j].getText(), "")){
            int num=Integer.parseInt(bt[i][j].getText());
            for (int l = 0; l < 9; l++){
                for (int m = 0; m < 9; m++) {
                    try{
                        if (Integer.parseInt(bt[l][m].getText()) == num) {
                            bt[l][m].setBackground(Color.gray);
                        }
                    }
                    catch (Exception e){}
                }
            }}

    }

    private void bgColorReset() {
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                bt[i][j].setBackground(Color.white);}}
    }

    private void textColorReset() {
        for(int a=0;a<9;a++){
            for (int b=0;b<9;b++){
                bt[a][b].setForeground(Color.black);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(qm[I][J]==0){
        if (flag==1)
        {
            //bt[I][J].setText("");
            String temp;
            for (int i=0;i<9;i++){
                for (int j=0;j<9;j++){
                    temp=bt[i][j].getText();
                    if (Objects.equals(temp, "")){
                        tar[i][j]=0;
                    }
                    else{
                        tar[i][j]=Integer.parseInt(temp);
                    }
                }
            }
            solveBoard(tar);
            flag++;
        }
        int v = e.getKeyCode();
        if ((v >= 49 && v <= 57) || (v >= 97 && v <= 105)) {
            if (v >= 49 && v <= 57)
                v -= 48;
            if (v >= 97 && v <= 105)
                v -= 96;
            bt[I][J].setText(v+"");}
        else if (v==8){
            bt[I][J].setText("");
            bt[I][J].setForeground(Color.black);
        }
        if (!"".equals(bt[I][J].getText())){colorChange(I,J);}
    }}

    private void colorChange(int i, int j) {
        if (Integer.parseInt(bt[i][j].getText())==tar[i][j]){
            bt[i][j].setForeground(new Color(0x3556E5));
        }
        else{
            bt[i][j].setForeground(Color.red);
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        //nothing
    }
    void get2Darray(){
        String temp;
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                temp=bt[i][j].getText();
                if (Objects.equals(temp, "")){
                    tar[i][j]=0;
                }
                else{
                    tar[i][j]=Integer.parseInt(temp);
                }
            }
        }
        solveBoard(tar);

        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                if (tar[i][j]==0){
                    bt[i][j].setText("");
                }
                else{
                    bt[i][j].setText(String.valueOf(tar[i][j]));
                }
            }
        }

    }

    void solveASingleCell(){
        if (flag==1)
        {
            bt[I][J].setText("");
            String temp;
            for (int i=0;i<9;i++){
                for (int j=0;j<9;j++){
                    temp=bt[i][j].getText();
                    if (Objects.equals(temp, "")){
                        tar[i][j]=0;
                    }
                    else{
                        tar[i][j]=Integer.parseInt(temp);
                    }
                }
            }
            solveBoard(tar);
            flag++;
        }
        bt[I][J].setForeground(Color.green);
        bt[I][J].setText(String.valueOf(tar[I][J]));
    }
    void clearBoard(){
        bgColorReset();
        textColorReset();
        for (int i=0;i<9;i++){
            for (int j=0;j<9;j++){
                bt[i][j].setText("");
            }
        }
    }




    // <-----------------------------Sudoku Solver-------------------------------->

    private  boolean isNumberInRow(int[][] tar, int number, int row) {
        for (int i = 0; i < 9; i++) {
            if (tar[row][i] == number) {
                return true;
            }
        }
        return false;
    }

    private  boolean isNumberInColumn(int[][] tar, int number, int column) {
        for (int i = 0; i < 9; i++) {
            if (tar[i][column] == number) {
                return true;
            }
        }
        return false;
    }

    private  boolean isNumberInBox(int[][] tar, int number, int row, int column) {
        int localBoxRow = row - row % 3;
        int localBoxColumn = column - column % 3;

        for (int i = localBoxRow; i < localBoxRow + 3; i++) {
            for (int j = localBoxColumn; j < localBoxColumn + 3; j++) {
                if (tar[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }

    private  boolean isValidPlacement(int[][] tar, int number, int row, int column) {
        return !isNumberInRow(tar, number, row) &&
                !isNumberInColumn(tar, number, column) &&
                !isNumberInBox(tar, number, row, column);
    }

    private  boolean solveBoard(int[][] tar) {

        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (tar[row][column] == 0) {
                    for (int numberToTry = 1; numberToTry <= 9; numberToTry++) {
                        if (isValidPlacement(tar, numberToTry, row, column)) {
                            tar[row][column] = numberToTry;
                            //fucked here

//                            bt[row][column].setText(String.valueOf(numberToTry));

                            if (solveBoard(tar)) {
                                return true;
                            }
                            else {
                                tar[row][column] = 0;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }


}
