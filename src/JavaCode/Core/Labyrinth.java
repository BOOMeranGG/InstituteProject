package JavaCode.Core;
import java.util.ArrayList;
import java.util.Stack;


public class Labyrinth {
    private int width;
    private int height;
    private Vec2d startPoint;
    private MatrixElements[][] matrix;

    Labyrinth(int width, int height, Vec2d startPoint){
        this.width = width;
        this.height = height;
        this.startPoint = startPoint;
        this.matrix = new MatrixElements[this.width*2-1][this.height*2-1];
        this.start();
    }

    private void start(){
        for (int i = 0;i < this.width*2-1; i++){
            for (int j = 0; j < this.height*2-1; j++) {
                this.matrix[i][j] = MatrixElements.WALL;
            }
        }
        for (int i = 1; i < this.width*2-1; i+=2){
            for (int j = 1; j < this.height*2-1; j+=2){
                this.matrix[i][j] = MatrixElements.CELL;
            }
        }
    }

    private ArrayList<Vec2d> getNeighbours(Vec2d point_coords){
        Vec2d topCoords = new Vec2d(point_coords.x, point_coords.y-2);
        Vec2d rightCoords = new Vec2d(point_coords.x+2, point_coords.y);
        Vec2d leftCoords = new Vec2d(point_coords.x-2, point_coords.y);
        Vec2d downCoords = new Vec2d(point_coords.x, point_coords.y+2);

        Vec2d[] list_of_coords = {topCoords, rightCoords, leftCoords, downCoords};

        ArrayList<Vec2d> neighbourList = new ArrayList<>();

        int length = this.matrix.length;
        for (Vec2d neighbour: list_of_coords){
            if (neighbour.y > 0 && neighbour.y < length && neighbour.x > 0 && neighbour.x < length){
                if (this.matrix[neighbour.y][neighbour.x] == MatrixElements.CELL){
                    neighbourList.add(neighbour);
                }
            }
        }
        return neighbourList;
    }

    private void removeWall(Vec2d first, Vec2d second){
        int xDiff = second.x - first.x;
        int yDiff = second.y - first.y;

        int addX = (xDiff != 0) ? (xDiff/Math.abs(xDiff)) : 0;
        int addY = (yDiff != 0) ? (yDiff/Math.abs(yDiff)) : 0;

        int targetX = first.x + addX;
        int targetY = first.y + addY;

        this.matrix[targetY][targetX] = MatrixElements.VISI;
    }

    private void setMode(MatrixElements mode, Vec2d coords){
        this.matrix[coords.y][coords.x] = mode;
    }

    MatrixElements[][] generate(){
        this.setMode(MatrixElements.VISI, this.startPoint);
        Vec2d currentPoint = startPoint;

        Stack<Vec2d> stack = new Stack<>();
        stack.push(currentPoint);

        while (!stack.isEmpty()){
            if (this.getNeighbours(currentPoint).size() > 0){
                ArrayList<Vec2d> neighbours = this.getNeighbours(currentPoint);
                int rand_num = (int)(Math.random() * neighbours.size());
                this.removeWall(currentPoint, neighbours.get(rand_num));
                currentPoint = neighbours.get(rand_num);
                this.setMode(MatrixElements.VISI, currentPoint);
                stack.push(currentPoint);
            }
            else{
                if (!stack.isEmpty()){
                    currentPoint = stack.pop();
                }
            }
        }
        return this.matrix;
    }

}
