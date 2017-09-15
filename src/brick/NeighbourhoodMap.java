package brick;

import java.awt.*;
import java.util.*;
import java.util.List;

import static java.util.stream.Collectors.toMap;

public class NeighbourhoodMap {
	private Map<String, Integer> coordsToNeigbourhood = new HashMap<>();
	private Map<Integer, List<String>> neigbourhoodBlocks = new HashMap<>();
	private int maxSizeNeighbourhood;

	public boolean hasNeighbourhood(int i, int j) {
		return getCoordsToNeigbourhood().containsKey(i + ";" + j);
	}

	public LinkedHashMap<Integer, List<String>> getSortedNeigbourhoodBlocks() {
		return neigbourhoodBlocks.entrySet().stream()
				.sorted((left, right) ->
						Integer.compare(right.getValue().size(), left.getValue().size()))
				.filter(e->e.getValue().size()>1)
				.collect(toMap(
						Map.Entry::getKey,
						Map.Entry::getValue,
						(a, b) -> {
							throw new AssertionError();
						},
						LinkedHashMap::new
				));

	}



	public void gatherNeighbours(Block[][] value) {
		for (int i = 0; i < 10; i++) {
			if (value[i] == null) {
				continue;
			}
			for (int j = 9; j > -1; j--) {
				if (value[i][j] == null) {
					continue;
				}
				fetchNeighbours(value, i, j, null);
			}
		}
	}

	public void fetchNeighbours(Block[][] value, int i, int j, Integer neigbourhoodId) {

		String blockKey = i + ";" + j;
		if (getCoordsToNeigbourhood().containsKey(blockKey))
			return;

		Color c = value[i][j].getColor();
		if (neigbourhoodId == null) {
			neigbourhoodId = getNeigbourhoodBlocks().size() + 1;
			getNeigbourhoodBlocks().put(neigbourhoodId, new ArrayList<String>());
		}
		getNeigbourhoodBlocks().get(neigbourhoodId).add(blockKey);
		getCoordsToNeigbourhood().put(blockKey, neigbourhoodId);


		if (isValidNeighbour(value, i + 1, j, c)) {
			fetchNeighbours(value, i + 1, j, neigbourhoodId);
		}

		if (isValidNeighbour(value, i - 1, j, c)) {
			fetchNeighbours(value, i - 1, j, neigbourhoodId);
		}

		if (isValidNeighbour(value, i, j + 1, c)) {
			fetchNeighbours(value, i, j + 1, neigbourhoodId);
		}

		if (isValidNeighbour(value, i, j - 1, c)) {
			fetchNeighbours(value, i, j - 1, neigbourhoodId);
		}

	}

	private boolean isValidNeighbour(Block[][] value, int i, int j, Color c) {
		return i > -1 && i < 10 &&
				j > -1 && j < 10 &&
				value[i] != null && value[i][j] != null && value[i][j].getColor().equals(c);
	}

	public Map<Integer, List<String>> getNextPlayCandidates() {
		Map<Integer, List<String>> nextBlockGroups = new HashMap<>();
		{
			for (Integer neighbourhoodNum : getNeigbourhoodBlocks().keySet()) {
				List<String> blocksGroup = getNeigbourhoodBlocks().get(neighbourhoodNum);
				if (blocksGroup.size() > 1) {
					nextBlockGroups.put(neighbourhoodNum, blocksGroup);
				}
			}
		}
		return nextBlockGroups;
	}

	public int getMaxSizeNeighbourhood() {
		return maxSizeNeighbourhood;
	}

	public void setMaxSizeNeighbourhood(int maxSizeNeighbourhood) {
		this.maxSizeNeighbourhood = maxSizeNeighbourhood;
	}

	public Map<String, Integer> getCoordsToNeigbourhood() {
		return coordsToNeigbourhood;
	}

	public void setCoordsToNeigbourhood(Map<String, Integer> coordsToNeigbourhood) {
		this.coordsToNeigbourhood = coordsToNeigbourhood;
	}

	public Map<Integer, List<String>> getNeigbourhoodBlocks() {
		return neigbourhoodBlocks;
	}

	public void setNeigbourhoodBlocks(Map<Integer, List<String>> neigbourhoodBlocks) {
		this.neigbourhoodBlocks = neigbourhoodBlocks;
	}

}
