package mm.mayorideas.objects;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.typeface.IIcon;

import mm.mayorideas.R;

public enum IdeaCategory {

    ENVIRONMENT(1, R.string.environment, FontAwesome.Icon.faw_leaf, android.R.color.holo_green_dark),
    CULTURE(2, R.string.culture, GoogleMaterial.Icon.gmd_palette, R.color.md_brown_400),
    TRANSPORT(3, R.string.transport, GoogleMaterial.Icon.gmd_directions_bus, R.color.mayorideas_blue_dark),
    INFRASTRUCTURE(4, R.string.infrastructure, FontAwesome.Icon.faw_road, R.color.md_grey_600);

    private final int id;
    private final int name;
    private final IIcon icon;
    private final int iconColorRes;

    IdeaCategory(int id, int name, IIcon icon, int iconColorRes) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.iconColorRes = iconColorRes;
    }

    public static IdeaCategory getIdeaCategory(int ID) {
        for (IdeaCategory category : IdeaCategory.values()) {
            if (ID == category.id) {
                return category;
            }
        }
        return null;
    }

    public int getNameRes() {
        return name;
    }

    public IIcon getIcon() {
        return icon;
    }

    public int getIconColorRes() {
        return iconColorRes;
    }

    public boolean equals(IdeaCategory category) {
        return this.id == category.id;
    }

    public int getID() {
        return this.id;
    }
}
