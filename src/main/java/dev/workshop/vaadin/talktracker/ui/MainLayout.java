package dev.workshop.vaadin.talktracker.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.PageTitle;

import java.util.Optional;

@Layout
public class MainLayout extends AppLayout implements AfterNavigationObserver {

    public static final String HEADER_TITLE = "Conference Talk Tracker";
    private final H2 title;

    public MainLayout() {
        title = new H2(HEADER_TITLE);
        addToNavbar(new DrawerToggle(), title);
        addToDrawer(new Scroller(createSideNav()));
    }

    private SideNav createSideNav() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Talks", "", VaadinIcon.LIST.create()));
        return nav;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        var viewTitle = getCurrentPageTitle(event);
        title.setText(viewTitle.orElse("Talk Tracker"));
    }

    private Optional<String> getCurrentPageTitle(AfterNavigationEvent event) {
        return event.getActiveChain().stream()
                .findFirst()
                .map(component -> component.getClass().getAnnotation(PageTitle.class))
                .map(PageTitle::value);
    }
}
