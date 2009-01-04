package ucbang.gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @author Ibrahim
 *
 */
public abstract class Clickable implements Comparable<Clickable>{

	public Rectangle rect;
	public Polygon bounds;
	//public int location; //position of card on field or in hand
	public int playerid;
	protected AffineTransform at;
	protected double theta=0.0;
	protected Clickable partner;
	protected BufferedImage img;
	//protected final BufferedImage sourceImg;
	protected boolean draggable = true;
	public boolean animating = false;
	public enum Animations {ROTATETO, MOVETO, GROW, SHRINK, FADEIN, FADEOUT};
	protected Animations animation;
	protected double rotateto=0.0;
	protected Point moveto;
	protected int xspeed, yspeed;
	/**
	 * @param r
	 */
	public Clickable(Polygon p, BufferedImage srcimg){
		bounds = p;
		rect = p.getBounds();
		if(srcimg!=null){
			img = new BufferedImage(srcimg.getWidth(), srcimg.getHeight(), srcimg.getType());
			img.getRaster().setRect(srcimg.getData());
			//sourceImg = img;
		}
	}
	public int compareTo(Clickable o) {
		if(o.rect.getLocation().y!=rect.getLocation().y)
			return ((Integer)rect.getLocation().y).compareTo(o.rect.getLocation().y);
		else
			return ((Integer)rect.getLocation().x).compareTo(o.rect.getLocation().x);
	}
	public void paint(Graphics2D g){
		if(img!=null)
			g.drawImage(img, rect.x, rect.y, null);
	}
	/**
	 * Moves the Clickable to the specified location
	 * @param x
	 * @param y
	 */
	public void move(int x, int y){
		int dx = x-rect.x;
		int dy = y-rect.y;
		if(at!=null)at.translate(rect.x-x, rect.y-y);
		rect.translate(dx, dy);
		bounds.translate(dx, dy);
		if(partner!=null){
			partner.translate(dx, dy);
		}
	}
	
	/**
	 * @param dx
	 * @param dy
	 */
	public void translate(int dx, int dy){
		rect.translate(dx, dy);
		//rect.translate(dx, dy);
		bounds.translate(dx, dy);
	}
	/**
	 * Sets the Clickable's partner.
	 * <p>If a Clickable has a partner defined, moving it will also
	 * translate the partner so that they move together.</p>
	 * @param partner the other Clickable to be set as the partner
	 */
	public void setPartner(Clickable partner){
		this.partner=partner;
	}

	private Point2D.Double getPolygonCenter(Polygon poly){
		// R + r = height
		Rectangle2D r2 = poly.getBounds2D();
		double cx = r2.getX() + r2.getWidth()/2;
		double cy = r2.getY() + r2.getHeight()/2;
		int sides = poly.xpoints.length;
		double side = Point2D.distance(poly.xpoints[0], poly.ypoints[0],
				poly.xpoints[1], poly.ypoints[1]);
		double R = side / (2 * Math.sin(Math.PI/sides));
		double r = R * Math.cos(Math.PI/sides);
		double dy = (R - r)/2;
		return new Point2D.Double(cx, cy + dy);
	}
	/**
	 * Rotates the Clickable the specified angle, in radians.
	 */
	public void rotate(double angle){
		double realrotation=(angle-theta)%(Math.PI*2);
		if(realrotation<0)realrotation+=Math.PI*2;
		if(realrotation>Math.PI*2)realrotation-=Math.PI*2;
		if(realrotation>0 && realrotation<(Math.PI*2)){
			Polygon p = rectToPoly(rect);

			at = AffineTransform.getRotateInstance(angle/2,
					rect.getCenterX(), rect.getCenterY());
			theta=angle%(Math.PI*2);
			Shape l = at.createTransformedShape(p);
			PathIterator iter=l.getPathIterator(at);
			int i=0;
			float[] pts= new float[6];
			p.reset();
			while(!iter.isDone()){
				int type = iter.currentSegment(pts);
				switch(type){
				case PathIterator.SEG_MOVETO :
					//System.out.println("SEG_MOVETO");
					p.addPoint((int)pts[0],(int)pts[1]);
					break;
				case PathIterator.SEG_LINETO :
					//System.out.println("SEG_LINETO");
					p.addPoint((int)pts[0],(int)pts[1]);
					break;
				}
				i++;
				iter.next();
			}
			//rect = p.getBounds();
			bounds = p;
		}
	}
	public Polygon rectToPoly(Rectangle r){
		int[] xs = {r.x, r.x, r.x+r.width, r.x+r.width};
		int[] ys = {r.y, r.y+r.height, r.y+r.height, r.y};
		Polygon temp = new Polygon(xs, ys, 4);
		return temp;
	}
	public void animate(){
		switch(animation){
		case ROTATETO:
			if(Math.abs(theta-rotateto)<=Math.PI/32){
				rotate(rotateto);
				animating = false;
				break;
			}
			if(theta>rotateto)
				rotate(theta-Math.PI/32);
			if(theta<rotateto)
				rotate(theta+Math.PI/32);
			break;
		case MOVETO:
			if(rect.y-moveto.y<yspeed){
				this.move(moveto.x, moveto.y);
				animating = false;
				break;
			}
			this.translate(xspeed, yspeed);
			break;
		case GROW:
			break;
		case SHRINK:
			break;
		}
	}
	public void rotateTo(double theta){
		animation = Animations.ROTATETO;
		animating = true;
		rotateto = theta;
	}
	public void moveTo(int x, int y){
		animation = Animations.MOVETO;
		animating = true;
		moveto =  new Point(x,y);
		yspeed = (y-rect.y)/10;
		xspeed = (x-rect.x)/10;
	}
	public abstract void fade(int amount);
}