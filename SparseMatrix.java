public class SparseMatrix implements SparseInterface {

	public int cur_rows = 5; // Default values for new Sparse Matrices
	public int cur_columns = 5;
	public MatrixNode head;
	public MatrixNode tail;

	class MatrixNode { // Node for Linked List of elements in Matrix
		int data;
		int row;
		int col;
		public MatrixNode next = null; // Default values for Matrix linked list
		public MatrixNode prev = null;

		public MatrixNode(int row, int col, int data) { // 3 argument constructor for Instantiating Matrix node
			this.data = data;
			this.row = row;
			this.col = col;
		}
	}

	public SparseMatrix() { // Default Constructor
		cur_rows = cur_columns = 5; // Sets size to 5x5
	}

	public SparseMatrix(int rows, int cols) { // 2 parameter Constructor for new sparse matrix
		setSize(rows, cols); // Sets size of NxM matrix
	}

	public boolean emptyMatrix() { // check if matrix is empty
		return (head == null);
	}

	@Override
	public void clear() { // set all matrix data to 0/null
		this.head = null;
	}

	/*
	 * Sets maximum size of the matrix. Number of rows. It should also clear the
	 * matrix (make all elements 0)
	 */
	@Override
	public void setSize(int numRows, int numCols) {
		cur_rows = numRows;
		cur_columns = numCols;
		clear();
	}

	/*
	 * Adds an element to the row and column passed as arguments (overwrites if
	 * element is already present at that position). Throws an error if row/column
	 * combination is out of bounds.
	 */

	public void addElement(int row, int col, int data) {
		// Throws exception if the index to add is out of bounds.
		if (row >= cur_rows || col >= cur_columns) {
			throw new IndexOutOfBoundsException("Index " + row + ", " + col + " is out of bounds!");
		}
		if ((row < 0) || (col < 0)) {
			throw new IndexOutOfBoundsException("Index " + row + ", " + col + " is out of bounds!");
		}

		MatrixNode myNewNode = new MatrixNode(row, col, data);
		if (myNewNode.data == 0) { // if passed in element is zero don't add it
			if (getElement(row, col) != 0) // if passed 0 to replace an old value, remove old value, do not add 0.
				removeElement(row, col);

			return;
		}
		// Matrix is empty
		if (emptyMatrix()) {
			this.head = myNewNode;
			this.head.next = null;
		}

		// not empty, we want to add to a matrix
		else {
			MatrixNode curr = this.head;

			while (curr.next != null) {
				// if it's in there replace it
				if (curr.row == row && curr.col == col) {
					curr.data = data;
					break;
				}

				if (row <= curr.row) { // First check if the new row is less than our iterator
					if (col <= curr.col || row < curr.row) //
					{
						if (curr.prev == null) { // Check that curr is the head
							myNewNode.next = curr;
							curr.prev = myNewNode;
							this.head = myNewNode; // Make our new node the head
							break;
						} else { // else switch nodes around appropriately
							curr.prev.next = myNewNode;
							myNewNode.next = curr;
							myNewNode.prev = curr.prev;
							curr.prev = myNewNode;
							break;
						}
					}
				}
				curr = curr.next; // iterate node
			}
			if (curr.next == null) { // is last node
				if (curr.row == row && curr.col == col) { // and data is to be added
					curr.data = data; // replace data
				} else if (row <= curr.row) { // else shuffle nodes appropriately
					if (col <= curr.col || row < curr.row) {
						if (curr.prev == null) {
							myNewNode.next = curr;
							curr.prev = myNewNode;
							this.head = myNewNode;
						} else {
							curr.prev.next = myNewNode;
							myNewNode.next = curr;
							myNewNode.prev = curr.prev;
							curr.prev = myNewNode;
						}
					} else {
						curr.next = myNewNode;
						myNewNode.next = null;
						myNewNode.prev = curr;
					}
				} else { // added node is last node case
					curr.next = myNewNode;
					myNewNode.next = null;
					myNewNode.prev = curr;
				}
			}

		}
	}

	/*
	 * Remove (make 0) the element at the specified row and column. Throws an error
	 * if row/column combination is out of bounds.
	 */

	public void removeElement(int row, int col) {
		if (row >= cur_rows || col >= cur_columns) { // out of bounds exception
			throw new IndexOutOfBoundsException("Index " + row + ", " + col + " is out of bounds!");
		}
		if ((row < 0) || (col < 0)) {
			throw new IndexOutOfBoundsException("Index " + row + ", " + col + " is out of bounds!");
		}

		// removes the node chosen
		MatrixNode curr = head; // creates iterator curr
		if (curr == null) // check for node not existing
			return; // return and don't remove anything
		if (curr.next == null && curr.row == row && curr.col == col) // if the head -> null, and is the item to be
																		// removed
			head = null; // remove the head node
		while (curr.next != null) {
			if (curr == head && curr.col == col && curr.row == row) { // if the head isn't the only node and is to be
																		// removed
				head = curr.next; // move the head over
				break;
			} else if ((curr.next.col == col) && (curr.next.row == row) && (curr.next.next == null)) // if the next node
																										// is to be
																										// removed and
																										// it's next is
																										// null
			{ // set to null don't point
				curr.next = null;
				break;
			} else if ((curr.next.col == col) && (curr.next.row == row)) { // otherwise
				curr.next = curr.next.next; // set pointers to skip one over
				curr.next.prev = curr; // set pointers to point back to prev

				break;
			}
			curr = curr.next;
		}
	}

	/*
	 * Return the element at the specified row and column Throws an error if
	 * row/column combination is out of bounds.
	 */
	@Override
	public int getElement(int row, int col) {

		if (row >= cur_rows || col >= cur_columns) { // checks for out of bounds exception
			throw new IndexOutOfBoundsException("Index " + row + ", " + col + " is out of bounds!");
		}
		if ((row < 0) || (col < 0)) {
			throw new IndexOutOfBoundsException("Index " + row + ", " + col + " is out of bounds!");
		}

		MatrixNode curr = this.head; // set iterator to head of list
		while (curr != null) { // iterate through
			if (curr.row == row && curr.col == col) { // return data if correct row/col
				return curr.data;
			}
			curr = curr.next;
		}
		return 0;
	}

	/*
	 * Should return the nonzero elements of your sparse matrix as a string. The
	 * String should be k lines, where k is the number of nonzero elements. Each
	 * line should be in the format "row column data" where row and column are the
	 * "coordinate" of the data and all are separated by spaces. An empty matrix
	 * should return an empty string. The print should be from left to right and
	 * from top to bottom (like reading a book) i.e. the matrix 3 0 1 0 2 0 0 0 4
	 * Should print as: 0 0 3 0 2 1 1 1 2 2 2 4
	 */
	@Override
	public String toString() {
		String output = ""; // set default to empty/all zero matrix
		MatrixNode curr = this.head; // iterate from head

		while (curr != null) {
			if (curr.data != 0) // if the data is 0/null don't output it
				output = output + curr.row + " " + curr.col + " " + curr.data + "\n";
			curr = curr.next;
		}
		return output;
	}
	/*
	 * Should return the size of the matrix.
	 */

	@Override
	public int getNumRows() {
		return this.cur_rows; // return object num of rows
	}

	@Override
	public int getNumCols() {
		return this.cur_columns; // return object num of cols
	}

	@Override
	public SparseInterface addMatrices(SparseInterface matrixToAdd) {
		if (matrixToAdd.getNumRows() != cur_rows || matrixToAdd.getNumCols() != cur_columns) { // ensure matrices are
																								// same dim
			return null;
		} else { // add if are same dims
			SparseInterface addedMatrix = new SparseMatrix(cur_rows, cur_columns); // creates new matrix to return
			MatrixNode curr = this.head;
			while (curr != null) { // cycle through this matrix list
				int elemToAdd = matrixToAdd.getElement(curr.row, curr.col) + curr.data; // add the elements in the list
																						// with the elems at that
																						// location in passed in matrix
				if (elemToAdd != 0) // as long as the elems added do not = 0, add the value to new matrix
					addedMatrix.addElement(curr.row, curr.col, elemToAdd);

				curr = curr.next;
			}
			return addedMatrix;
		}
	}

	@Override
	public SparseInterface multiplyMatrices(SparseInterface matrixToMultiply) {
		if (matrixToMultiply.getNumRows() != cur_columns) { // check mxn nxp = mxp
			return null;
		} else {
			SparseInterface multipliedMatrix = new SparseMatrix(cur_rows, matrixToMultiply.getNumCols()); // creates new
																											// matrix to
																											// return

			int newData = 0; // initialize value to be added
			for (int i = 0; i < this.cur_rows; i++) { // outer loop iterates though matrix A rows, to move to next row
				for (int j = 0; j < matrixToMultiply.getNumCols(); j++) { // iterates through Matrix B columns, to move
																			// to next col
					for (int k = 0; k < this.cur_columns; k++) { // iterates though A columns to multiply and add to bs
																	// cols
						newData += this.getElement(i, k) * matrixToMultiply.getElement(k, j);
						multipliedMatrix.addElement(i, j, newData); // Add element to our new matrix

					}
					newData = 0; // reset the data field
				}
			}
			return multipliedMatrix;
		}
	}

}
