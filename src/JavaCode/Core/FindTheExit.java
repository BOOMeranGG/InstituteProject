package JavaCode.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FindTheExit {
    private MatrixElements matrix[][];
    private int x1;
    private int y1;
    public int x2;
    public int y2;

//TODO: ЧЕТКО
    public MatrixElements[][] getMatrix() {
        return matrix;
    }

//TODO: ЧЕТКО
    FindTheExit(MatrixElements[][] matrix, int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.matrix = matrix;
    }

    /**
     * основная логика поиска пути
     */
//TODO: ЧЕТКО
    public void findExit(){
        Cell currentCell = new Cell(x1, y1);    //начальная ячейка
        matrix[currentCell.getX()][currentCell.getY()] = MatrixElements.VISI;   //отмечаем как посещенная
        Cell neighbourCell; //ячейка сосед
        List<Cell> listNeighbours;  //Список соседей
        Stack<Cell> stack = new Stack<>();

        do {
            listNeighbours = getNeighbours(currentCell);    // Проверяем соседей (количество, расположение)
            if (listNeighbours.size() != 0) {   //Если у клетки есть непосещённые соседи
                int randN = (int) (Math.random() * listNeighbours.size());  //Рандомно выбираем куда пойдем
                neighbourCell = listNeighbours.get(randN);  //List возращает случчайно выбранного соседа
                stack.push(currentCell);    //Помещаем ячейку, где возник выбор пути
                currentCell = neighbourCell;    //Произошло перемещение ячейки
                matrix[currentCell.getX()][currentCell.getY()] = MatrixElements.VISI;//Помечаем как посещенную
            } else if (!stack.empty()) {    //Если стек НЕ пуст
                matrix[currentCell.getX()][currentCell.getY()] = MatrixElements.BADROAD;    //Отмечаем ячейку ведущию в тупик
                currentCell = stack.pop();  //Достаем ячейку, на которой мы делали выбор
            } else {
                //Если нет соседей и точек в стеке, но не все точки посещены
                //выбираем случайную из непосещенных
                System.out.println("Выхода нет((\n");
                break;
            }
        } while (!isExit(currentCell));
    }

    /**
     *метод, который проверяет, является ли переданная ячейка Cell(напоминаю,
     *что Cell содержит координаты Х и У) выходом из лабиринта.
     *Метод возвращает true, если выход найден
     */
//TODO: ЧЕТКО
    private boolean isExit(Cell cell) {
        /*if ( (x2 == cell.getX()) && (y2 == cell.getY())){
          return true;
        }
        return false;*/
        if (cell.getX() == matrix.length - 2 && cell.getY() == matrix[0].length - 2) {
            return cell.getX() == matrix.length - 2 && cell.getY() == matrix[0].length - 2;}
        return cell.getX() == matrix.length - 2 && cell.getY() == matrix[0].length - 2;
    }

    /**
     * в качестве параметра вы передаёте ячейку.
     * Метод проводит некоторые действия и выясняет,
     * какие НЕПОСЕЩЁННЫЕ соседи есть у этой ячейки и
     * возвращает List<Cell> - список со всеми непосещёнными соседями ROAD
     * @param c
     * @return
     */
//TODO: Есть подозрение, что логика храмает
    private List<Cell> getNeighbours(Cell c) {
        List<Cell> cell_list = new ArrayList<>();   //Создали переменную для возврата данных
        int i = c.getX();
        int j = c.getY();

        if (i - 1 > 0 && matrix[i - 1][j] == MatrixElements.ROAD)               //Проверка на верхнего соседа
            cell_list.add(new Cell(i - 1, j));
        if (j + 1 < ((matrix[0].length * 2 + 1) * 2) && matrix[i][j + 1] == MatrixElements.ROAD)     //Проверка на правого соседа
            cell_list.add(new Cell(i, j + 1));
        if (i + 1 < ((matrix.length * 2 + 1) * 2) && matrix[i + 1][j] == MatrixElements.ROAD)    //Проверка на нижнего соседа
            cell_list.add(new Cell(i + 1, j));
        if (j - 1 > 0 && matrix[i][j - 1] == MatrixElements.ROAD)               //Проверка на левого соседа
            cell_list.add(new Cell(i, j - 1));
        return cell_list;
    }
}


