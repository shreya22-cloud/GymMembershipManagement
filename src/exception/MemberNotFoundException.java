package exception;

/**
 * Custom exception thrown when a member lookup fails.
 * Used when searching by ID and the member does not exist.
 */
public class MemberNotFoundException extends Exception {

    private final String searchKey;

    /**
     * Constructor with search key (member ID or name).
     *
     * @param searchKey The value that was used in the search.
     */
    public MemberNotFoundException(String searchKey) {
        super("No member found with identifier: " + searchKey);
        this.searchKey = searchKey;
    }

    /**
     * Returns the key used during the failed search.
     */
    public String getSearchKey() {
        return searchKey;
    }
}
