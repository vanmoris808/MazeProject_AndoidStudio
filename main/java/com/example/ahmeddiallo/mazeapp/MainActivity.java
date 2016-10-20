package com.example.ahmeddiallo.mazeapp;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Scanner;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {


    TextView display;
    EditText xStart;
    EditText yStart;
    EditText xEnd;
    EditText yEnd;
    Button button;

    final char V = '.';
    private char[][] maze = {
            {'*', '*', '*', '*', '*', '*'},
            {'*', ' ', ' ', ' ', ' ', '*'},
            {'*', ' ', '*', '*', ' ', '*'},
            {'*', ' ', ' ', ' ', ' ', '*'},
            {'*', ' ', '*', '*', ' ', '*'},
            {'*', ' ', ' ', '*', ' ', '*'},
            {'*', ' ', ' ', '*', ' ', '*'},
            {'*', '*', '*', '*', '*', '*'},
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        xStart = (EditText) findViewById(R.id.xStart);
        yStart = (EditText) findViewById(R.id.yStart);
        xEnd = (EditText) findViewById(R.id.xEnd);
        yEnd = (EditText) findViewById(R.id.yEnd);
        button = (Button) findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int xS=Integer.parseInt(xStart.getText().toString());
                int yS=Integer.parseInt(yStart.getText().toString());
                int xE=Integer.parseInt(xEnd.getText().toString());
                int yE=Integer.parseInt(yEnd.getText().toString());

                findRoute(xS, yS, xE, yE);

                Snackbar snackbar = Snackbar.make(v, "Please wait...", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Undo", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "End", Toast.LENGTH_SHORT).show();
                            }
                        });
                snackbar.show();
            }

        });

    }

    public int size() {
        return maze.length;
    }

    public void print() {
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                System.out.print(maze[i][j]);
                System.out.print(' ');
                display = (TextView) findViewById(R.id.display);
                display.setText("(" + i + " ," + j + ")");
            }
            System.out.println();
        }
    }

    public char mark(int i, int j, char value) {
        assert (isInMaze(i, j));
        char tmp = maze[i][j];
        maze[i][j] = value;
        return tmp;
    }

    public char mark(MazePos pos, char value) {
        return mark(pos.i(), pos.j(), value);
    }

    public boolean isMarked(int i, int j) {
        assert (isInMaze(i, j));
        return (maze[i][j] == V);
    }

    public boolean isMarked(MazePos pos) {
        return isMarked(pos.i(), pos.j());
    }

    public boolean isClear(int i, int j) {
        assert (isInMaze(i,j));
        return (maze[i][j] !='*' && maze[i][j] != V);
    }
    public boolean isClear(MazePos pos) {
        return isClear(pos.i(), pos.j());
    }

    public boolean isInMaze(int i, int j) {
        if (i >= 0 && i < size() && j >= 0 && j < size()) {
            return true;
        } else return false;
    }

    public boolean isInMaze(MazePos pos) {
        return isInMaze(pos.i(), pos.j());
    }


    public boolean isFinal(MazePos pos, int xE, int yE) {
        return (pos.i()==xE && pos.j()== yE);
    }

    public char[][] clone() {
        char[][] mazeCopy = new char[size()][size()];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                mazeCopy[i][j] = maze[i][j];
            }
        }
        return mazeCopy;
    }

     public void findRoute(int xS, int yS, int xE, int yE){

         Stack<MazePos> n = new Stack<>();

         n.push(new MazePos(xS, yS));

         MazePos curPos,next;
         while(!n.empty()){

             curPos=n.pop();
             if(isFinal(curPos, xE, yE)) break;

             mark(curPos, V);

             next = curPos.north();
             if(isInMaze(next) && isClear(next)){
                 n.push(next);}
             else {
                 next = curPos.east();
                 if (isInMaze(next) && isClear(next)) {
                     n.push(next);
                 }
                 else {
                     next = curPos.west();
                     if (isInMaze(next) && isClear(next)) {
                         n.push(next);
                     }
                     else {
                         next = curPos.south();
                         if (isInMaze(next) && isClear(next)) {
                             n.push(next);
                         }
                         //put this in a toast
                         else
                         {
                             display = (TextView) findViewById(R.id.display);
                             display.setText("You are stuck. No route found.");
                         }
                     }
                 }
             }
             }

         if(!n.empty()){

             print();
             display = (TextView) findViewById(R.id.display);
             display.setText("Maze solved, Path using Stack:\n");
             findRoute(xS,yS,xE,yE);

         }else display = (TextView) findViewById(R.id.display);
         display.setText("You are stuck");
         print();
     }
    class MazePos{
        int i, j;
        public MazePos(int i, int j){
            this.i = i;
            this.j = j;
        }

        public int i(){return i;}
        public int j(){return j;}

        //Coordinate for the North, South, East and West

        public MazePos north(){
            return new MazePos(i-1, j);
        }
        public MazePos south(){
            return new MazePos(i+1,j);
        }
        public MazePos east(){
            return new MazePos(i,j+1);
        }
        public MazePos west(){
            return new MazePos(i, j-1);
        }
    }

}