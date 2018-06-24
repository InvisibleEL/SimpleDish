package com.airsaid.warmjacket.bean;

import java.io.Serializable;
import java.util.List;


public class CookCategoryBean extends BaseBean<CookCategoryBean> {

    private CategoryInfoBean categoryInfo;
    private List<ChildsBean> childs;

    public CategoryInfoBean getCategoryInfo() {
        return categoryInfo;
    }

    public void setCategoryInfo(CategoryInfoBean categoryInfo) {
        this.categoryInfo = categoryInfo;
    }

    public List<ChildsBean> getChilds() {
        return childs;
    }

    public void setChilds(List<ChildsBean> childs) {
        this.childs = childs;
    }

    public static class CategoryInfoBean implements Serializable{
        private String ctgId;
        private String name;

        public String getCtgId() {
            return ctgId;
        }

        public void setCtgId(String ctgId) {
            this.ctgId = ctgId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class ChildsBean implements Serializable{

        private CategoryInfoBean categoryInfo;
        private List<ChildsBean> childs;

        public CategoryInfoBean getCategoryInfo() {
            return categoryInfo;
        }

        public void setCategoryInfo(CategoryInfoBean categoryInfo) {
            this.categoryInfo = categoryInfo;
        }

        public List<ChildsBean> getChilds() {
            return childs;
        }

        public void setChilds(List<ChildsBean> childs) {
            this.childs = childs;
        }

        public static class CategoryInfoBean implements Serializable {
            private String ctgId;
            private String name;
            private String parentId;

            public String getCtgId() {
                return ctgId;
            }

            public void setCtgId(String ctgId) {
                this.ctgId = ctgId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }
        }

    }
}
