package JavaCode.Grafics;

import JavaCode.Core.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class MapPanel extends JPanel {

    private MapMouseListener mapMouseListener;
    private final int WIDTH = 500;
    private final int HEIGHT = 500;
    private int width;
    private int height;
    private int housesNum;
    private int speed;
    private int x1;
    private int y1;
    private int squareSize;
    private MatrixElements[][] field;
    private ArrayList<Cell> houses;
    private Cell houseBeginPoint;
    private Cell houseEndPoint;
    private Labyrinth mapGenerator;
    private FindTheExit wayFinder;
    private List<Cell> way;
    private boolean manBool;
    private Thread thread;
    private int manId;

    private Image grass = new ImageIcon("res/map/grass.png").getImage();
    private Image roadH = new ImageIcon("res/map/roadH.png").getImage();
    private Image roadV = new ImageIcon("res/map/roadV.png").getImage();
    private Image roadRB = new ImageIcon("res/map/roadRB.png").getImage();
    private Image roadRT = new ImageIcon("res/map/roadRT.png").getImage();
    private Image roadLB = new ImageIcon("res/map/roadLB.png").getImage();
    private Image roadLT = new ImageIcon("res/map/roadLT.png").getImage();
    private Image roadR = new ImageIcon("res/map/roadR.png").getImage();
    private Image roadB = new ImageIcon("res/map/roadB.png").getImage();
    private Image roadL = new ImageIcon("res/map/roadL.png").getImage();
    private Image roadT = new ImageIcon("res/map/roadT.png").getImage();
    private Image roadX = new ImageIcon("res/map/roadX.png").getImage();
    private Image houseR = new ImageIcon("res/map/houseR.png").getImage();
    private Image houseB = new ImageIcon("res/map/houseB.png").getImage();
    private Image houseL = new ImageIcon("res/map/houseL.png").getImage();
    private Image houseT = new ImageIcon("res/map/houseT.png").getImage();
    private Image houseBegin = new ImageIcon("res/map/houseBegin.png").getImage();
    private Image houseEnd = new ImageIcon("res/map/houseEnd.png").getImage();
    private Image wayImg = new ImageIcon("res/map/way.png").getImage();
    private Image manImg = new ImageIcon("res/iconBtnMan.png").getImage();


    MapPanel() {
        generateMap(15, 15, 30);
        thread = new Thread();
        regenerate();
        mapMouseListener = new MapMouseListener(this, houses, x1, y1, squareSize);
        addMouseListener(mapMouseListener);
        setSpeed(1);
    }

    void generateMap(int width, int height, int housesNum) {
        this.width = width;
        this.height = height;
        this.housesNum = housesNum;
    }

    void setSpeed(int speed) {
        this.speed = speed;
    }

    void regenerate() {
        manBool = false;
        thread.stop();
        mapGenerator = new Labyrinth(width / 2, height / 2);
        wayFinder = null;
        mapGenerator.generate();
        mapGenerator.generateHouses(housesNum);
        field = mapGenerator.getMatrix();
        houses = getHouses();
        randBeginHouse();
        randEndHouse();
        repaint();
    }

    void randBeginHouse() {
        clearWay();
        int randNum = (int) (Math.random() * houses.size());
        houseBeginPoint = houses.get(randNum);
        repaint();
    }

    void randEndHouse() {
        clearWay();
        int randNum = (int) (Math.random() * houses.size());
        houseEndPoint = houses.get(randNum);
        repaint();
    }

    void displayWay() {
        Cell begin = getRoad(houseBeginPoint);
        Cell end = getRoad(houseEndPoint);
        wayFinder = new FindTheExit(field, begin.x, begin.y, end.x, end.y);
        wayFinder.findExit();
        repaint();
    }

    void displayMan() {
        if (!manBool && wayFinder != null) {
            manBool = true;
            way = wayFinder.getRoadList();
            thread = new Thread(() -> {
                for (int k = 0; k < way.size(); k++) {
                    manId = k;
                    repaint();
                    try {
                        Thread.sleep(1000 / speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                manBool = false;
                repaint();
            });
            thread.start();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawField(g);
    }

    private void drawField(Graphics g) {
        squareSize = Math.min(WIDTH / width, HEIGHT / height);
        x1 = (WIDTH - width * squareSize) / 2;
        y1 = (HEIGHT - height * squareSize) / 2;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                switch (field[i][j]) {
                    case ROAD:
                        drawRoad(g, i, j);
                        break;
                    case BADROAD:
                        drawRoad(g, i, j);
                        break;
                    case WALL:
                        g.drawImage(grass, x1 + i * squareSize, y1 + j * squareSize,
                                squareSize, squareSize, null);
                        break;
                    case HOUSE:
                        drawHouse(g, i, j);
                        break;
                    case VISI:
                        drawRoad(g, i, j);
                        g.drawImage(wayImg, x1 + i * squareSize, y1 + j * squareSize,
                                squareSize, squareSize, null);
                        break;
                }
            }
        }
        g.drawImage(houseBegin, x1 + houseBeginPoint.x * squareSize,
                y1 + houseBeginPoint.y * squareSize, squareSize, squareSize, null);
        g.drawImage(houseEnd, x1 + houseEndPoint.x * squareSize,
                y1 + houseEndPoint.y * squareSize, squareSize, squareSize, null);
        if (manBool) {
            drawMan(g);
        }
        mapMouseListener.initialize(houses, x1, y1, squareSize);
    }

    private void drawMan(Graphics g) {
        g.drawImage(manImg, x1 + way.get(manId).x * squareSize,
                y1 + way.get(manId).y * squareSize, squareSize, squareSize, null);
    }

    private void drawRoad(Graphics g, int i, int j) {
        //дорога только справа и слева от дороги -> дорога горизонтальная
        if (right(i, j) && left(i, j) && !bottom(i, j) && !top(i, j)) {
            g.drawImage(roadH, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только сверху и снизу от дороги -> дорога вертикальная
        } else if (top(i, j) && bottom(i, j) && !right(i, j) && !left(i, j)) {
            g.drawImage(roadV, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только справа и снизу от дороги
        } else if (right(i, j) && bottom(i, j) && !left(i, j) && !top(i, j)) {
            g.drawImage(roadRB, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только справа и сверху от дороги
        } else if (right(i, j) && top(i, j) && !left(i, j) && !bottom(i, j)) {
            g.drawImage(roadRT, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только слева и снизу от дороги
        } else if (left(i, j) && bottom(i, j) && !right(i, j) && !top(i, j)) {
            g.drawImage(roadLB, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только слева и сверху от дороги
        } else if (left(i, j) && top(i, j) && !right(i, j) && !bottom(i, j)) {
            g.drawImage(roadLT, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только справа, слева и снизу от дороги
        } else if (right(i, j) && left(i, j) && bottom(i, j)) {
            g.drawImage(roadT, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только справа, слева и сверху от дороги
        } else if (right(i, j) && left(i, j) && top(i, j)) {
            g.drawImage(roadB, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только справа, снизу и сверху от дороги
        } else if (right(i, j) && bottom(i, j) && top(i, j)) {
            g.drawImage(roadL, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога только слева, снизу и сверху от дороги
        } else if (left(i, j) && bottom(i, j) && top(i, j)) {
            g.drawImage(roadR, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога горизонтальная
        } else if (right(i, j) || left(i, j)) {
            g.drawImage(roadH, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //дорога вертикальная
        } else if (bottom(i, j) || top(i, j)) {
            g.drawImage(roadV, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
            //перекрёсток или единственная дорога
        } else {
            g.drawImage(roadX, x1 + i * squareSize, y1 + j * squareSize,
                    squareSize, squareSize, null);
        }
    }

    private void drawHouse(Graphics g, int i, int j) {
        g.drawImage(grass, x1 + i * squareSize, y1 + j * squareSize,
                squareSize, squareSize, null);
        Cell house = new Cell(i, j);
        Cell road = getRoad(house);
        if (road.x - house.x == 1) {
            g.drawImage(houseR, x1 + house.x * squareSize, y1 + house.y * squareSize,
                    squareSize, squareSize, null);
        }
        if (road.x - house.x == -1) {
            g.drawImage(houseL, x1 + house.x * squareSize, y1 + house.y * squareSize,
                    squareSize, squareSize, null);
        }
        if (road.y - house.y == 1) {
            g.drawImage(houseB, x1 + house.x * squareSize, y1 + house.y * squareSize,
                    squareSize, squareSize, null);
        }
        if (road.y - house.y == -1) {
            g.drawImage(houseT, x1 + house.x * squareSize, y1 + house.y * squareSize,
                    squareSize, squareSize, null);
        }
    }

    private boolean right(int i, int j) {
        return i < width - 1 &&
                (field[i + 1][j].equals(MatrixElements.ROAD)
                        || field[i + 1][j].equals(MatrixElements.VISI)
                        || field[i + 1][j].equals(MatrixElements.BADROAD));
    }

    private boolean bottom(int i, int j) {
        return j < height - 1
                && (field[i][j + 1].equals(MatrixElements.ROAD)
                || field[i][j + 1].equals(MatrixElements.VISI)
                || field[i][j + 1].equals(MatrixElements.BADROAD));
    }

    private boolean left(int i, int j) {
        return i > 0
                && (field[i - 1][j].equals(MatrixElements.ROAD)
                || field[i - 1][j].equals(MatrixElements.VISI)
                || field[i - 1][j].equals(MatrixElements.BADROAD));
    }

    private boolean top(int i, int j) {
        return j > 0
                && (field[i][j - 1].equals(MatrixElements.ROAD)
                || field[i][j - 1].equals(MatrixElements.VISI)
                || field[i][j - 1].equals(MatrixElements.BADROAD));
    }

    public void click(int id, boolean isLeftClick) {
        if (isLeftClick) {
            houseBeginPoint = houses.get(id);
        } else {
            houseEndPoint = houses.get(id);
        }
        clearWay();
        repaint();
    }

    private ArrayList<Cell> getHouses() {
        ArrayList<Cell> local = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (field[i][j].equals(MatrixElements.HOUSE)) {
                    local.add(new Cell(i, j));
                }
            }
        }
        return local;
    }

    private Cell getRoad(Cell house) {
        if (top(house.x, house.y)) {
            return new Cell(house.x, house.y - 1);
        }
        if (right(house.x, house.y)) {
            return new Cell(house.x + 1, house.y);
        }
        if (bottom(house.x, house.y)) {
            return new Cell(house.x, house.y + 1);
        }
        if (top(house.x, house.y)) {
            return new Cell(house.x - 1, house.y);
        }
        String message = "Нет дороги около дома в точке [" + house.x + ", " + house.y + "]";
        throw new RuntimeException(message);
    }

    private void clearWay() {
        thread.stop();
        manBool = false;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (field[i][j].equals(MatrixElements.VISI)
                        || field[i][j].equals(MatrixElements.BADROAD)) {
                    field[i][j] = MatrixElements.ROAD;
                }
            }
        }
    }
}

