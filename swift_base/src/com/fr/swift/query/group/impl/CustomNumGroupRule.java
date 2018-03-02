package com.fr.swift.query.group.impl;

import com.fr.swift.structure.Pair;
import com.fr.swift.structure.array.IntList;
import com.fr.swift.structure.array.IntListFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anchore
 * @date 2018/2/28
 */
public class CustomNumGroupRule extends BaseGroupRule {
    static final NumberFormat NUMBER_FORMAT = new DecimalFormat("#.##");
    private List<NumInterval> intervals;
    private String otherGroupName;

    private Map<Integer, Pair<String, IntList>> map = new HashMap<Integer, Pair<String, IntList>>();

    public CustomNumGroupRule(List<NumInterval> intervals, String otherGroupName) {
        super();
        this.intervals = intervals;
        this.otherGroupName = otherGroupName;
    }

    @Override
    public String getGroupName(int index) {
        return map.get(index).getKey();
    }

    @Override
    public IntList map(int index) {
        return map.get(index).getValue();
    }

    @Override
    void initMap() {
        for (int i = 0; i < dictColumn.size(); i++) {
            Number num = (Number) dictColumn.getValue(i);
            int index = findIndex(num);

            String groupName;
            if (index != -1) {
                // 在区间里
                groupName = intervals.get(index).name;
            } else {
                if (hasOtherGroup()) {
                    // 有其他组，则全部分到其他
                    index = intervals.size();
                    groupName = otherGroupName;
                } else {
                    // 不在区间里，又没有其他分组，则单独为一组
                    index = map.size();
                    groupName = NUMBER_FORMAT.format(num);
                }
            }

            if (map.containsKey(index)) {
                map.get(index).getValue().add(i);
            } else {
                IntList indices = IntListFactory.createIntList();
                indices.add(i);
                map.put(index, Pair.of(groupName, indices));
            }
        }
    }

    private int findIndex(Number num) {
        for (int i = 0, size = intervals.size(); i < size; i++) {
            if (intervals.get(i).contains(num.doubleValue())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int newSize() {
        return map.size();
    }

    private boolean hasOtherGroup() {
        return otherGroupName != null;
    }

    public static class NumInterval {
        /**
         * 是否为大于等于
         */
        private boolean greaterOrEq;
        /**
         * 下界
         */
        private double floor;
        /**
         * 是否为小于等于
         */
        boolean lessOrEq;
        /**
         * 上界
         */
        private double ceil;

        private String name;

        public NumInterval(String name, double floor, boolean greaterOrEq, double ceil, boolean lessOrEq) {
            this.greaterOrEq = greaterOrEq;
            this.floor = floor;
            this.lessOrEq = lessOrEq;
            this.ceil = ceil;
            this.name = name;
        }

        private boolean contains(double val) {
            if (Double.compare(val, floor) == 0 && greaterOrEq) {
                return true;
            }
            if (val > floor && val < ceil) {
                return true;
            }
            return Double.compare(val, ceil) == 0 && lessOrEq;
        }
    }
}