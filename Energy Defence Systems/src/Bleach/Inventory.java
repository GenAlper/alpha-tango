package Bleach;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
	private int hotbarCount; // How many inventory slots are reserved for the
								// hotbar.
	private int size; // How many items the inventory can hold.
	private List<InventoryItem> items; // List of items in the inventory.

	public Inventory() {
		hotbarCount = 10;
		size = 45;
		items = new ArrayList<InventoryItem>();
	}

	public boolean addItem(InventoryItem item) {
		if (items.size() < size) {
			items.add(item);
			return true;
		} else {
			return false;
		}
	}

	public InventoryItem getItem(int index) {
		if (index < 0 || index >= items.size()) {
			return null;
		} else {
			return items.get(index);
		}
	}
}
