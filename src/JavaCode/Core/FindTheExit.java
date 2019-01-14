package JavaCode.Core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import static JavaCode.Core.MatrixElements.*;

public class FindTheExit {
    private MatrixElements matrix[][];
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private List<Cell> roadList; //Путь, которому прошел

    public MatrixElements[][] getMatrix() {
        return matrix;
    }

    public FindTheExit(MatrixElements[][] matrix, int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.matrix = matrix;
    }

    public List<Cell> getRoadList() {
        return roadList;
    }

    /**
     * Основная логика поиска пути
     */
    public void findExit() {
        Cell currentCell = new Cell(x1, y1);
        matrix[currentCell.getX()][currentCell.getY()] = VISI;
        Cell neighbourCell;
        List<Cell> listNeighbours;
        Stack<Cell> stack = new Stack<>();

        do {
            listNeighbours = getNeighbours(currentCell);
            if (listNeighbours.size() != 0) {                                   //Если есть куда идти - идём
                int randN = (int) (Math.random() * listNeighbours.size());
                neighbourCell = listNeighbours.get(randN);
                stack.push(currentCell);
                currentCell = neighbourCell;
                matrix[currentCell.getX()][currentCell.getY()] = VISI;
            } else if (!stack.empty()) {                                        //Иначе, возвращаемся назад
                matrix[currentCell.getX()][currentCell.getY()] = BADROAD;
                currentCell = stack.pop();
            } else {
                //Если нет соседей и точек в стеке, но не все точки посещены
                //выбираем случайную из непосещенных
                System.err.println("Выхода нет((\n");
                break;
            }
        } while (!isExit(currentCell));

        stack.add(new Cell(x2, y2));
        roadList = new ArrayList<>(stack);
    }

    /**
     * Возвращает true, если переданная клетка является выходом
     */
    private boolean isExit(Cell cell) {
        return (x2 == cell.getX()) && (y2 == cell.getY());
    }

    /**
     * @return - список непосещённых соседей
     */
    private List<Cell> getNeighbours(Cell c) {
        List<Cell> cell_list = new ArrayList<>();
        int i = c.getX();
        int j = c.getY();

        if (i - 1 > 0 && matrix[i - 1][j] == ROAD)                                  //Проверка на верхнего соседа
            cell_list.add(new Cell(i - 1, j));
        if (j + 1 < ((matrix[0].length * 2 + 1) * 2) && matrix[i][j + 1] == ROAD)   //Проверка на правого соседа
            cell_list.add(new Cell(i, j + 1));
        if (i + 1 < ((matrix.length * 2 + 1) * 2) && matrix[i + 1][j] == ROAD)      //Проверка на нижнего соседа
            cell_list.add(new Cell(i + 1, j));
        if (j - 1 > 0 && matrix[i][j - 1] == ROAD)                                  //Проверка на левого соседа
            cell_list.add(new Cell(i, j - 1));
        return cell_list;
    }
}


