package autohubb.vinture.com.autohubb.model;




public class AddCart {
    public static final String TABLE_NAME = "addcart";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CARNAME = "carname";
    public static final String COLUMN_VINNO = "vinno";

    private int id;
    private String carname;
    private String vinno;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CARNAME + " TEXT,"
                    + COLUMN_VINNO + " TEXT"
                    + ")";

    public AddCart() {
    }

    public AddCart(int id, String carname, String vinno) {
        this.id = id;
        this.carname = carname;
        this.vinno = vinno;
    }

    public int getId() {
        return id;
    }

    public String getCarName() {
        return carname;
    }

    public void setCarName(String carname) {
        this.carname = carname;
    }

    public String getVinNo() {
        return vinno;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVinNo(String vinNo) {
        this.vinno = vinNo;
    }
}
