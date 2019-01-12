package JavaCode.Grafics;

import JavaCode.Core.Cell;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

class MapMouseListener implements MouseListener {

    private MapPanel mapPanel;
    private ArrayList<Cell> houses;
    private int x1;
    private int y1;
    private int squareSize;
    private int id;

    MapMouseListener(MapPanel mapPanel, ArrayList<Cell> houses, int x1, int y1, int squareSize) {
        this.mapPanel = mapPanel;
        initialize(houses, x1, y1, squareSize);
    }

    public void initialize(ArrayList<Cell> houses, int x1, int y1, int squareSize) {
        this.houses = houses;
        this.x1 = x1;
        this.y1 = y1;
        this.squareSize = squareSize;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < houses.size(); i++) {
            if (e.getX() < x1 + (houses.get(i).x + 1) * squareSize &&
                    e.getX() > x1 + houses.get(i).x * squareSize &&
                    e.getY() < y1 + (houses.get(i).y + 1) * squareSize &&
                    e.getY() > y1 + houses.get(i).y * squareSize) {
                id = i;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getX() < x1 + (houses.get(id).x + 1) * squareSize &&
                e.getX() > x1 + houses.get(id).x * squareSize &&
                e.getY() < y1 + (houses.get(id).y + 1) * squareSize &&
                e.getY() > y1 + houses.get(id).y * squareSize) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    mapPanel.click(id, true);
                    break;
                case MouseEvent.BUTTON3:
                    mapPanel.click(id, false);
                    break;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

