package com.stiven.rumahhangeul.entity;

import java.util.List;

public interface UserProjection {
    Long getId();
    String getNamaDepan();
    String getNamaBelakang();
    Long getScore();
    Long getPoint();
    String getBorderUsed();
    String getProfileUsed();
}
