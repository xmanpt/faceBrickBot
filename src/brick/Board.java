package brick;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

public class Board implements Comparable<Board> {

	public Board() {

	}

	public Board(Map<Color, Integer> colorsCount) {
		for (Color c : colorsCount.keySet()) {
			this.colorsCount.put(c, colorsCount.get(c));
		}
	}

	public static Set<String> FAILED = new HashSet<String>();
	private Block[][] value = new Block[10][10];
	private Map<Color, Integer> colors = new HashMap<Color, Integer>();
	private int score = 0;
	private Map<Color, Integer> colorsCount = new HashMap<Color, Integer>();
	private List<String> bestSolutionKey;
	private Board bestSolution;
	private Integer bestSolutionScore;
	private int size = 0;
	private NeighbourhoodMap neighbourhoodMap = new NeighbourhoodMap();

	public void addBlock(int x, int y, Block b) {
		// System.out.println(x+ " "+ y);
		value[x][y] = b;
		this.size++;
		if (!colors.containsKey(b.getColor())) {
			colors.put(b.getColor(), colors.size());
		}
		if (!colorsCount.containsKey(b.getColor())) {
			colorsCount.put(b.getColor(), 1);
		} else {
			colorsCount.put(b.getColor(), colorsCount.get(b.getColor()) + 1);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("###################\n");
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {
				if (value[i] != null && value[i][j] != null) {
					sb.append(colors.get(value[i][j].getColor()) + " ");
				} else
					sb.append(". ");
			}
			sb.append("\n");
		}
		sb.append("###################");
		return sb.toString();
	}

	public void gatherNeighbours(){
		getNeighbourhoodMap().gatherNeighbours(value);
	}

	public int process() {
		// is solved
		if (getSize() == 0) {
			return getScore();
		}
		if (hasLoners())
			return -1;

		gatherNeighbours();

		for (List<String> blockGroup : getNeighbourhoodMap().getSortedNeigbourhoodBlocks().values()) {
			if(blockGroup.size()<2)
				continue;


			Board clone = clone();
			clone.removeBlockGroup(blockGroup);
			String cloneID = clone.getId();
			if (!Board.FAILED.contains(cloneID)) {
				int cloneResult = clone.process();
				if (cloneResult > -1) {
					if (getBestSolutionScore() == null || cloneResult > getBestSolutionScore()) {
						setBestSolution(clone);
						setBestSolutionScore(cloneResult);
						setBestSolutionKey(blockGroup);
						return getBestSolutionScore();
					}
				} else {
					Board.FAILED.add(cloneID);
					if(Board.FAILED.size() % 100000 ==0){
						System.out.print((char) (new Random().nextInt(26) + 'A'));
					}

				}

			}

		}

		return (getBestSolution() == null ? -2 : getBestSolutionScore());
	}

	protected void removeBlockGroup(List<String> blocksGroup) {
		this.score += blocksGroup.size() * (blocksGroup.size() - 1);
		for (String id : blocksGroup) {
			String[] coordsAsArr = id.split(";");
			int i = Integer.valueOf(coordsAsArr[0]);
			int j = Integer.valueOf(coordsAsArr[1]);
			removeBlock(i, j);
		}

		putGravity();
		putHorizontalGravity();

	}



	private boolean hasLoners() {
		for (Integer i : getColorsCount().values()) {
			if (i == 1) {
				return true;
			}
		}
		return false;
	}

	private void putHorizontalGravity() {
		for (int i = 0; i < 9; i++) {
			if (isColumnBlank(i)) {
				dropColumn(i);
			}

		}
	}

	private void dropColumn(int column) {
		boolean hasColumnWithValuesAfterwards = false;
		for (int i = column; i < 9; i++) {
			value[i] = value[i + 1];
			if (!isColumnBlank(i)) {
				hasColumnWithValuesAfterwards = true;
			}
		}
		value[9] = null;

		if (hasColumnWithValuesAfterwards && isColumnBlank(column)) {
			dropColumn(column);
		}
	}

	private boolean isColumnBlank(int i) {
		for (int j = 0; j < 10; j++) {
			if (value[i] != null && value[i][j] != null) {
				return false;
			}
		}
		return true;
	}

	private void putGravity() {
		for (int i = 0; i < 10; i++) {
			putGravityLine(i);
		}
	}

	private void putGravityLine(int i) {
		Integer firstBlank = null;
		Integer lastValue = null;
		for (int j = 9; j > -1; j--) {
			if (value[i][j] == null) {
				if (firstBlank == null)
					firstBlank = j;
			} else {
				lastValue = j;
			}
		}
		if (firstBlank != null && lastValue != null && lastValue < firstBlank) {
			dropBlankSpace(i, firstBlank);
			putGravityLine(i);
		}

	}

	private void dropBlankSpace(Integer i, Integer firstBlank) {
		for (int j = firstBlank; j > 0; j--) {
			value[i][j] = value[i][j - 1];
			value[i][j - 1] = null;
		}
	}

	public Board clone() {
		Board n = new Board(getColorsCount());
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {
				if (value[i] != null && value[i][j] != null) {
					n.addBlock(i, j, value[i][j]);
				}
			}
		}
		n.setScore(getScore());

		return n;
	}

	private Block removeBlock(int i, int j) {
		if (value[i][j] == null)
			return null;

		Block removedBlock = value[i][j];
		Color c = value[i][j].getColor();
		colorsCount.put(c, colorsCount.get(c) - 1);
		this.size--;
		value[i][j] = null;

		return removedBlock;
	}


	public void removeGroupByBlock(int i, int j) {
		gatherNeighbours();

		Integer neighbourhoodID = getNeighbourhoodMap().getCoordsToNeigbourhood().get(i + ";" + j);
		List<String> neighbourhood = getNeighbourhoodMap().getNeigbourhoodBlocks().get(neighbourhoodID);
		if(neighbourhood.size()<2)
			return;

		removeBlockGroup(neighbourhood);
	}



	@Override
	public int compareTo(Board o) {
		if (getColors().size() != o.getColors().size())
			return -1;

		if (getSize() != o.getSize())
			return -1;

		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {
				if (value[i][j] == null && o.getValue()[i][j] != null)
					return -1;
				if (value[i][j] != null && o.getValue()[i][j] == null)
					return -1;
				if (value[i][j] != null && o.getValue()[i][j] != null
						&& value[i][j].getColor() != o.getValue()[i][j].getColor())
					return -1;
			}
		}

		return 0;

	}

	public void checkSolution() {
		if (getBestSolution() != null)
			System.out.println("GR8 SUCCESS! " + getScore());
		else
			System.out.println(":(");
	}

	public NeighbourhoodMap getNeighbourhoodMap() {
		return neighbourhoodMap;
	}

	public void setNeighbourhoodMap(NeighbourhoodMap neighbourhoodMap) {
		this.neighbourhoodMap = neighbourhoodMap;
	}

	public List<String> getBestSolutionKey() {
		return bestSolutionKey;
	}

	public void setBestSolutionKey(List<String> bestSolutionKey) {
		this.bestSolutionKey = bestSolutionKey;
	}

	public Board getBestSolution() {
		return bestSolution;
	}

	public void setBestSolution(Board bestSolution) {
		this.bestSolution = bestSolution;
	}

	public Integer getBestSolutionScore() {
		return bestSolutionScore;
	}

	public void setBestSolutionScore(Integer bestSolutionScore) {
		this.bestSolutionScore = bestSolutionScore;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Map<Color, Integer> getColorsCount() {
		return colorsCount;
	}

	public void setColorsCount(Map<Color, Integer> colorsCount) {
		this.colorsCount = colorsCount;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Block[][] getValue() {
		return value;
	}

	public void setValue(Block[][] value) {
		this.value = value;
	}

	public Map<Color, Integer> getColors() {
		return colors;
	}

	public void setColors(Map<Color, Integer> colors) {
		this.colors = colors;
	}

	public String getId() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			if (value[i] == null) {
				sb.append("-");
			} else {
				for (int j = 0; j < 10; j++) {
					if (value[i] != null && value[i][j] != null) {
						sb.append(colors.get(value[i][j].getColor()));
					}
					sb.append(".");
				}
			}
			sb.append(";");
		}

		return sb.toString();
	}

	public void setSolutions(Map<String, Board> solutions) {
		solutions = solutions;
	}


}
