package com.growatt.grohome;

import com.tuya.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService;

/**
 * This is a sample code
 *
 */
public class BizBundleFamilyServiceImpl extends AbsBizBundleFamilyService {

    private long mHomeId;

    @Override
    public long getCurrentHomeId() {
        return mHomeId;
    }

    @Override
    public void setCurrentHomeId(long homeId) {
        mHomeId = homeId;
    }
}
