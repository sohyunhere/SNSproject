package dduwcom.mobile.simple_sns.listener;

import dduwcom.mobile.simple_sns.PostInfo;

public interface OnPostListener {
    void onDelete(PostInfo postInfo);
    void onModify();
}
