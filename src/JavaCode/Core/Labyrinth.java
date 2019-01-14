package JavaCode.Core;

import java.util.*;

public class Labyrinth {
    private int height;
    private int width;
    private MatrixElements matrix[][];

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public MatrixElements[][] getMatrix() {
        return matrix;
    }

    public Labyrinth(int height, int width) {
        this.height = height;
        this.width = width;
        matrix = new MatrixElements[height * 2 + 1][width * 2 + 1];
    }

    /**
     * Основная логика.
     */
    public void generate() {
        Cell currentCell = new Cell(1, 1);
        Cell neighbourCell;
        List<Cell> listNeighbours;
        Stack<Cell> stack = new Stack<>();

        generateEmptyMatrix();
        matrix[currentCell.getX()][currentCell.getY()] = MatrixElements.ROAD;
        do {
            listNeighbours = getNeighbours(currentCell);
            if (listNeighbours.size() != 0) {  //Если у клетки есть непосещённые соседи
                    int randN = (int) (Math.random() * listNeighbours.size());
                    neighbourCell = listNeighbours.get(randN);
                    stack.push(currentCell);
                    removeWall(currentCell, neighbourCell);
                    currentCell = neighbourCell;
                    matrix[currentCell.getX()][currentCell.getY()] = MatrixElements.ROAD;
            } else if (!stack.empty()) {
                currentCell = stack.pop();
            } else {
                //Если нет соседей и точек в стеке, но не все точки посещены
                //выбираем случайную из непосещенных
                System.err.println("Мисье, я удивлён, как этот код оказался здесь. Вероятно, у вас ошибка!\n");
                break;
            }
        } while (getUnvisitedCount());
    }

    /**
     * \
     * Создаёт начальную матрицу со стенами
     */
    private void generateEmptyMatrix() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if ((i % 2 != 0 && j % 2 != 0) && (i < matrix.length - 1    //Если ячейка нечётная по x и y
                        && j < matrix[i].length))                           //И находится в пределах стен лабиринта
                    matrix[i][j] = MatrixElements.CELL;                     //То это - клетка
                else
                    matrix[i][j] = MatrixElements.WALL;                     //В остальных случаях - стена
            }
        }
    }

    /**
     * @param c - текущая клетка
     * @return - возвращает список непосещённых соседей
     */
    private List<Cell> getNeighbours(Cell c) {
        List<Cell> cell_list = new ArrayList<>();
        int i = c.getX();
        int j = c.getY();

        if (i - 2 > 0 && matrix[i - 2][j] == MatrixElements.CELL)               //Проверка на верхнего соседа
            cell_list.add(new Cell(i - 2, j));
        if (j + 2 < (width * 2) && matrix[i][j + 2] == MatrixElements.CELL)     //Проверка на правого соседа
            cell_list.add(new Cell(i, j + 2));
        if (i + 2 < (height * 2) && matrix[i + 2][j] == MatrixElements.CELL)    //Проверка на нижнего соседа
            cell_list.add(new Cell(i + 2, j));
        if (j - 2 > 0 && matrix[i][j - 2] == MatrixElements.CELL)               //Проверка на левого соседа
            cell_list.add(new Cell(i, j - 2));

        return cell_list;
    }

    /**
     * Идём с одного и не включая последний - т.к. границу не проверяем
     * @return - Возвращает true, если остались непосещённые клетки
     */
    private boolean getUnvisitedCount() {
        for (int i = 1; i < matrix.length - 1; i++) {
            for (int j = 1; j < matrix[i].length - 1; j++) {
                if ((i % 2 != 0 && j % 2 != 0) && (i < matrix.length - 1 //Если ячейка нечётная по x и y
                        && j < matrix[i].length - 1))
                    if (matrix[i][j] == MatrixElements.CELL)
                        return true;
            }
        }
        return false;
    }

    /**
     * Данный метод удаляет Wall между first и second ячейкой в matrix
     * А на их месте ставит Road
     * ~~Так же указывает направление на месте CELL~~
     * @param first  - текущая ячейка
     * @param second - ячейка, в которую мы переходим
     */
    private void removeWall(Cell first, Cell second) {
        int x_1 = first.getX();
        int y_1 = first.getY();
        int x_2 = second.getX();
        int y_2 = second.getY();
        int newX = x_1 - x_2;
        int newY = y_1 - y_2;

        if (newX != 0) {
            matrix[(x_1 + x_2) / 2][y_1] = MatrixElements.ROAD;
        } else if (newY != 0) {
            matrix[x_1][(y_1 + y_2) / 2] = MatrixElements.ROAD;
        }
    }

    /**
     * Считаем количество стен - периметр. Определяем шанс поформуле:
     * 1 к (количество стен/количество домов). В цикле проходим по всем элементам,
     * определяя случайное число в диапазоне (0..chance). Если он = 1то ставим здесь дом.
     * Цикл бесконечный, и если все дома не расставлены, снова начинаем с первой клетки.
     * Если количество домов стало равным 0, выходим по метке. По периметру дома не генерируются!
     * @param countOfHouses - количество домов, которые необходимо сгенерировать
     */
    public void generateHouses(int countOfHouses) {
        int countOfWall = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == MatrixElements.WALL)
                    countOfWall++;
            }
        }
        countOfWall -= (2 * (matrix.length + matrix[0].length));
        if (countOfHouses > countOfWall)                                    //Если количество домов задано слишком много
            countOfHouses = countOfWall - 1;

        int chance = (countOfWall / countOfHouses);
        if (chance == 1)
            chance = 2;
        exitlabel:
        while (true) {
            for (int i = 1; i < matrix.length - 1; i++) {
                for (int j = 1; j < matrix[0].length - 1; j++) {
                    if (matrix[i][j] != MatrixElements.WALL)                //Если не стена, то пропускаем
                        continue;
                    int randN = (int) (Math.random() * chance);             //Рандомим число
                    if (randN == 1) {
                        if (matrix[i - 1][j] == MatrixElements.WALL &&      //Если стены по всем 4ём сторонам вокруг
                                matrix[i][j + 1] == MatrixElements.WALL &&
                                matrix[i + 1][j] == MatrixElements.WALL &&
                                matrix[i][j - 1] == MatrixElements.WALL) {
                            continue;                                       //То у дома нет выхода к дороге, не подходит
                        }
                        countOfHouses--;
                        matrix[i][j] = MatrixElements.HOUSE;
                        if (countOfHouses == 0)                             //Если дома закончились, выход
                            break exitlabel;
                    }
                }
            }
        }
    }
}
