package com.SerenityBDDForGemini.support;

import net.serenitybdd.core.pages.PageObject;

import java.lang.reflect.Field;

public class PageObjectOperations extends PageObject {
    private final String BASE_PATH = "com.SerenityBDDForGemini.pages.";

    public String poeName(String fieldName) {
        // Replacing spaces and other characters with underscore
        // Replace a hash(#) character with string "NUMBER" as it denotes
        fieldName = fieldName
                .replace('?', '_')
                .replace('/', '_')
                .replace('+', '_')
                .replace('>', '_')
                .replace('.', '_')
                .replace('<', '_')
                .replace("&", "AND")
                .replace('-', '_')
                .replace('(', '_')
                .replace(") ", "_")
                .replace(')', '_')
                .replace("#", "NUMBER")
                .replace(' ', '_')
                .replace("__", "") // Remove any leading or trailing double underscores
                .toUpperCase();
        if (fieldName.endsWith("_")) {
            fieldName = fieldName.substring(0, fieldName.length() - 1);
        }
        return fieldName;
    }

    public String pageObjectName(String pageName) {
        return pageName
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "")
                .replace("&", "And");
    }

    public Class<?> getPageClass(String pageObjectName) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(pageObjectName));
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new RuntimeException("Unable to find " + pageObjectName(pageObjectName)
                    + " class under package: " + BASE_PATH + "Perhaps class not created or inaccessible.");
        }
    }

    public Class<?> getPageClass(String pageObjectName, String workflow) {
        return getPageClass(pageObjectName(workflow).toLowerCase() + "."
                + pageObjectName(pageObjectName));
    }

    public Field poeFieldClass(String poeName, String pageObjectClassName, String workflow) {
        Class<?> pageClass = getPageClass(pageObjectClassName, workflow);
        poeName = poeName(poeName);
        try {
            return pageClass.getField(poeName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + poeName + " field in the class: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase()
                    + "." + pageObjectName(pageObjectClassName) + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + poeName + " field in the class: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    "." + pageObjectName(pageObjectClassName) + ". Perhaps element access is not allowed.");
        }
    }

    public Field poeFieldClass(String poeName, String pageObjectClassName) {
        Class<?> pageClass = getPageClass(pageObjectClassName);
        poeName = poeName(poeName);
        try {
            return pageClass.getField(poeName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + poeName + " field in the class: " +
                    BASE_PATH + pageObjectName(pageObjectClassName)
                    + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + poeName + " field in the class: "
                    + BASE_PATH + pageObjectName(pageObjectClassName)
                    + ". Perhaps element access is not allowed.");
        }
    }

    public Class<?> getSectionClass(String section, String workflow) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(workflow)
                    .toLowerCase() + ".section." + pageObjectName(section));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + pageObjectName(section) + " class under package: " +
                    BASE_PATH + pageObjectName(workflow).toLowerCase() + ".section. Perhaps " +
                    "class not created or inaccessible.");
        }
    }

    public Field poeSectionClass(String sectionName, String sectionObjectClassName, String workflow) {
        Class<?> sectionClass = getSectionClass(sectionObjectClassName, workflow);
        try {
            return sectionClass.getDeclaredField(sectionName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + sectionName + " field in the class: " + BASE_PATH
                    + pageObjectName(workflow).toLowerCase() + ".section." + sectionObjectClassName
                    + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + sectionName + " field in the class: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() + ".section." +
                    sectionObjectClassName + ". Perhaps element access is not allowed.");
        }
    }

    public Class<?> getGridClass(String grid, String workflow) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(workflow).toLowerCase() + ".grids." +
                    pageObjectName(grid));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + pageObjectName(grid) + " class under package: " + BASE_PATH
                    + pageObjectName(workflow).toLowerCase() + ".grids. Perhaps class not created or inaccessible.");
        }
    }

    public Field poeGridClass(String gridField, String gridName, String workflow) {
        Class<?> gridClass = getGridClass(gridName, workflow);
        try {
            return gridClass.getDeclaredField(gridField);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + gridField + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    ".grids." + gridName + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + gridName + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    ".grids." + gridName + ". Perhaps element access is not allowed.");
        }
    }

    public Class<?> getDialogClass(String dialog, String workflow) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(workflow).toLowerCase() + ".dialogs." +
                    pageObjectName(dialog));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + pageObjectName(dialog) + " class under package: " + BASE_PATH
                    + pageObjectName(workflow).toLowerCase() + ".dialogs. Perhaps class not created or inaccessible.");
        }
    }

    public Field poeDialogClass(String dialogField, String dialogName, String workflow) {
        Class<?> dialogClass = getDialogClass(dialogName, workflow);
        try {
            return dialogClass.getDeclaredField(dialogField);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + dialogField + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    ".dialogs." + dialogName + ". Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + dialogName + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() +
                    ".grids." + dialogName + ". Perhaps element access is not allowed.");
        }
    }

    public Class<?> getTabClass(String tab, String page, String workflow) {
        try {
            return Class.forName(BASE_PATH + pageObjectName(workflow).toLowerCase() + ".tabs." +
                    pageObjectName(page).toLowerCase() + "." + pageObjectName(tab));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find " + pageObjectName(tab) + " class under package: " + BASE_PATH
                    + pageObjectName(workflow).toLowerCase() + ".tabs." +
                    pageObjectName(page).toLowerCase() + ". Perhaps class not created or inaccessible.");
        }
    }

    public Field poeTabClass(String tabField, String tabName, String pageName, String workflow) {
        Class<?> dialogClass = getTabClass(tabName, pageName, workflow);
        try {
            return dialogClass.getDeclaredField(tabField);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to find " + tabField + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() + ".tabs." +
                    pageObjectName(pageName).toLowerCase() + ".Perhaps element does not exist.");
        } catch (SecurityException e) {
            throw new RuntimeException("Unable to access " + tabField + " field under package: "
                    + BASE_PATH + pageObjectName(workflow).toLowerCase() + ".tabs." +
                    pageObjectName(pageName).toLowerCase() + ". Perhaps element access is not allowed.");
        }
    }
}
