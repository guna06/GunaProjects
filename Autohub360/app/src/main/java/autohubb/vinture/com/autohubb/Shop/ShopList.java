package autohubb.vinture.com.autohubb.Shop;

public class ShopList {


    public String id = "";
    public String name = "";
    public String description = "";
    public String categoryId = "";
    public String typeId = "";
    public String currentStock = "";
    public String image = "";
    public String price = "";
    public String brand = "";
    public String partno = "";
    public String included = "";
   // public String categoryName = "";
    public String productCategory = "";
    public String productType = "";




    public ShopList(String id, String name, String description, String categoryId, String typeId, String currentStock, String image, String price,  String productCategory, String productType, String brand, String partno, String included) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.currentStock = currentStock;
        this.image = image;
        this.price = price;
        //this.categoryName = categoryName;
        this.productCategory = productCategory;
        this.productType = productType;
        this.brand=brand;
        this.partno=partno;
        this.included=included;

    }
}
