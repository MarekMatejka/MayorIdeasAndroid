package mm.mayorideas.api.listeners;

import java.util.List;

public interface SimpleNumberListListener {

    void onSuccess(List<Integer> values);
    void onFailure();

}
