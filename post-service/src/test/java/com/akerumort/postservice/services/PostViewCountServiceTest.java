package com.akerumort.postservice.services;

import com.akerumort.postservice.entities.PostViewCount;
import com.akerumort.postservice.services.repo.PostViewCountRepoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostViewCountServiceTest {

    @Mock
    private PostViewCountRepoService postViewCountRepoService;

    @InjectMocks
    private PostViewCountService postViewCountService;

    private PostViewCount postViewCount;

    @BeforeEach
    void setUp() {
        postViewCount = new PostViewCount();
        postViewCount.setPostId(1L);
        postViewCount.setViewCount(10);
    }

    @Test
    void testIncrementViewCount() {
        when(postViewCountRepoService.findViewCountById(anyLong())).thenReturn(postViewCount);
        when(postViewCountRepoService.saveViewCount(any(PostViewCount.class))).thenReturn(postViewCount);

        PostViewCount result = postViewCountService.incrementViewCount(1L);

        assertNotNull(result);
        assertEquals(11, result.getViewCount());
        verify(postViewCountRepoService, times(1)).findViewCountById(1L);
        verify(postViewCountRepoService, times(1)).saveViewCount(postViewCount);
    }

    @Test
    void testGetViewCount() {
        when(postViewCountRepoService.findViewCountById(anyLong())).thenReturn(postViewCount);

        int result = postViewCountService.getViewCount(1L);

        assertEquals(10, result);
        verify(postViewCountRepoService, times(1)).findViewCountById(1L);
    }

}
