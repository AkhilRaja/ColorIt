package arkratos.gamedev.com.colirfy;

/**
 * Created by akhilraja on 29/12/17.
 */

public class Categories {

    private String categoryname;
    private String image;
    private String id;
    private ImageGroup imageGroup;


    public Categories() {
    }
    public Categories(String categoryname,String image,String id)
    {
        this.id = id;
        this.categoryname = categoryname;
        this.image = image;
    }

    public String getCategoryname()
    {
        return categoryname;
    }
    public String getImage()
    {
        return image;
    }
    public String getId()
    {
        return id;
    }
    public ImageGroup getImageGroup() {
        return imageGroup;
    }
}
