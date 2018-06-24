package com.airsaid.warmjacket.bean;

import java.io.Serializable;
import java.util.List;


public class MenuDetailedBean extends BaseBean<MenuDetailedBean> {

    private List<String> ctgids;
    private String ctgtitles;
    private String menuid;
    private String name;
    private Recipe recipe;
    private String thumbnail;

    public void setCtgids(List<String> ctgids) {
        this.ctgids = ctgids;
    }

    public List<String> getCtgids() {
        return ctgids;
    }

    public void setCtgtitles(String ctgtitles) {
        this.ctgtitles = ctgtitles;
    }

    public String getCtgtitles() {
        return ctgtitles;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public class Recipe implements Serializable{

        private String img;
        private String ingredients;
        private String method;
        private String sumary;
        private String title;

        public void setImg(String img) {
            this.img = img;
        }

        public String getImg() {
            return img;
        }

        public void setIngredients(String ingredients) {
            this.ingredients = ingredients;
        }

        public String getIngredients() {
            return ingredients;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }

        public void setSumary(String sumary) {
            this.sumary = sumary;
        }

        public String getSumary() {
            return sumary;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

    }
}
