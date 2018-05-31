package com.fr.swift.segment;

import com.fr.swift.source.SourceKey;

import java.util.List;

/**
 * @author pony
 * @date 2017/10/16
 * 管理本机的单个分片的segment
 */
public interface SwiftSegmentManager {
    Segment getSegment(SegmentKey key);

    List<Segment> getSegment(SourceKey sourceKey);

    List<SegmentKey> getSegmentKeys(SourceKey sourceKey);

    boolean isSegmentsExist(SourceKey key);
}