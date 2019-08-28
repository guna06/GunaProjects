package autohubb.vinture.com.autohubb.model;

public class VehicleParts {


    public String id = "";
    public String name = "";
    public String description = "";
    public String categoryId = "";
    public String typeId = "";
    public String currentStock = "";
    public String image = "";
    public String price = "";
    public String productCategory = "";
    public String productType = "";
    public String CategoryName = "";



    public VehicleParts(String id, String name, String description, String categoryId, String typeId, String currentStock, String image, String price, String productCategory, String productType, String CategoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.currentStock = currentStock;
        this.image = image;
        this.price = price;
        this.productCategory = productCategory;
        this.productType = productType;
        this.CategoryName = CategoryName;
    }
}
