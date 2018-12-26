package com.saltwater.baseproject.module.update;

import com.TrampolineSchedulerRule;
import com.saltwater.baseproject.module.update.view.UpdateView;
import com.saltwater.baseproject.module.update.presenter.UpdatePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

/**
 * <pre>
 *     author : wenxin
 *     e-mail : wenxin2233@outlook.com
 *     time   : 2018/03/26
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class UpdatePresenterTest {
    @Rule
    public TrampolineSchedulerRule rule = new TrampolineSchedulerRule();

    private UpdatePresenter updatePresenter;
    @Mock
    private UpdateView.View view;

    @Mock
    private LifecycleProvider<ActivityEvent> provider;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {

    }



    @Test
    public void loadUpdate() throws Exception {

        updatePresenter.loadUpdate();
        verify(view).setUpdateInfo(anyString());
    }



}

