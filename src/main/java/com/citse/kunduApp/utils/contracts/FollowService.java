package com.citse.kunduApp.utils.contracts;

import com.citse.kunduApp.entity.Follow;

public interface FollowService {
    Follow save(int from, int to);

}
