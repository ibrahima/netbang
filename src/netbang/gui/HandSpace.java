package netbang.gui;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class HandSpace extends Clickable {
    public ArrayList<CardSpace> cards = new ArrayList<CardSpace>();
    public ArrayList<CardSpace> fieldCards = new ArrayList<CardSpace>();
    CardSpace character, hp;
    boolean autoSort = true;
    double theta;

    /**
     * @param r
     * @param player
     * @param theta
     */
    public HandSpace(Polygon p, int player, double theta) {
        super(p, new BufferedImage(10, 10, BufferedImage.TYPE_BYTE_BINARY));
        // TODO: Find some suitable image for a handplacer
        playerid = player;
        this.theta = theta;
    }

    /**
     * @param character
     * @param hp
     */
    public void setCharHP(CardSpace character, CardSpace hp) {
        this.character = character;
        this.hp = hp;
    }

    /**
     * @param card
     */
    public void addCard(CardSpace card) {
        if (!card.field)
            cards.add(card);
        else
            fieldCards.add(card);
    }

    /**
     * @return
     */
    public CardSpace removeLast() {
        return cards.remove(cards.size() - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see netbang.gui.Field.Clickable#move(int, int)
     */
    public void move(int x, int y) {
        int dx = x - rect.x;
        int dy = y - rect.y;
        super.move(x, y);
        Iterator<CardSpace> iter = cards.iterator();
        while (iter.hasNext()) {
            iter.next().translate(dx, dy);
        }
        // add a special boolean here
        iter = fieldCards.iterator();
        while (iter.hasNext()) {
            iter.next().translate(dx, dy);
        }

        if (character != null)
            character.translate(dx, dy);
        if (hp != null)
            hp.translate(dx, dy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see netbang.gui.Field.Clickable#translate(int, int)
     */
    public void translate(int dx, int dy) {
        super.translate(dx, dy);
        Iterator<CardSpace> iter = cards.iterator();
        while (iter.hasNext()) {
            iter.next().translate(dx, dy);
        }
        // add a special boolean here
        iter = fieldCards.iterator();
        while (iter.hasNext()) {
            iter.next().translate(dx, dy);
        }
        if (character != null)
            character.translate(dx, dy);
        if (hp != null)
            hp.translate(dx, dy);
    }

    public void sortHandSpace() {
        for (int n = 0; n < cards.size(); n++) {
            double handoffset = 30 * n;
            int xoffset = (int) (handoffset * Math.sin(theta));
            int yoffset = (int) (handoffset * Math.cos(theta));
            int x = (int) rect.x + rect.width - xoffset;
            int y = (int) rect.y + yoffset;
            CardSpace cs = cards.get(n);
            if (cs.animation!=0 && (cs.animation & MOVETO) !=0)
                continue;
            cs.rect.x = x;
            cs.rect.y = y;
        }
        for (int n = 0; n < fieldCards.size(); n++) {
            int fieldoffset = 100;
            double handoffset = 30 * n;
            int xoffset = (int) (handoffset * Math.sin(theta))
                    + (int) (fieldoffset * Math.sin(theta));
            int yoffset = (int) (handoffset * Math.cos(theta))
                    + (int) (fieldoffset * Math.cos(theta));
            int x = (int) rect.x + rect.width - xoffset;
            int y = (int) rect.y + yoffset;
            CardSpace cs = fieldCards.get(n);
            if (cs.animation!=0 && (cs.animation & MOVETO) !=0)
                continue;
            cs.rect.x = x;
            cs.rect.y = y;
        }
    }

    public void paint(Graphics2D g) {
        if (autoSort)
            g.fill3DRect(rect.x, rect.y, rect.width, rect.height, true);
        else
            g.draw3DRect(rect.x, rect.y, rect.width, rect.height, true);
    }

    public void fade() {
        // TODO Auto-generated method stub

    }
}