package com.vaadin.tutorial.dmdproject.crud;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.tutorial.dmdproject.PaperForm;
import com.vaadin.tutorial.dmdproject.backend.Paper;
import com.vaadin.tutorial.dmdproject.backend.PaperService;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

/**
 * Created by bbr on 28.10.15.
 */
public class CrudView extends CssLayout implements View {

    public static final String VIEW_NAME = "DBLP"; //digital
    private Grid paperList;
    private PaperForm paperForm;

    private CrudLogic crudLogic = new CrudLogic(this);
    private MButton newPaper;
    private MButton searchPaper;
    private TextField filter;

    private PaperService service = new PaperService();

    public CrudView() {
        setSizeFull();
        addStyleName("crud-view");
        HorizontalLayout topLayout = createTopBar();

        BeanItemContainer<Paper> myBean = new BeanItemContainer<Paper>(Paper.class);
        paperList = new Grid(myBean);
        paperList.setColumnOrder("name", "title", "type", "year");
        paperList.removeColumn("key");
        paperList.removeColumn("type");
        paperList.removeColumn("url");
        paperList.removeColumn("year");
        paperList.removeColumn("mdate");
        paperList.getColumn("name").setExpandRatio(1);
        paperList.getColumn("title").setExpandRatio(3);
        paperList.setSizeFull(); //TODO
        paperList.setSelectionMode(Grid.SelectionMode.SINGLE);
        paperList.addSelectionListener(e
                -> paperForm.edit((Paper) paperList.getSelectedRow()));
        refreshPapers();
        setIt();

        //paperList.setContainerDataSource(new BeanItemContainer<>(Paper.class, service.selectPapers()));

        VerticalLayout barAndListLayout = new VerticalLayout();
        barAndListLayout.addComponent(topLayout);
        barAndListLayout.addComponent(paperList);
        barAndListLayout.setMargin(true);
        barAndListLayout.setSpacing(true);
        barAndListLayout.setSizeFull();
        barAndListLayout.setExpandRatio(paperList, 1);
        barAndListLayout.setStyleName("crud-main-layout");

        addComponent(barAndListLayout);
        addComponent(paperForm);

        crudLogic.init();
    }

    private HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setStyleName("filter-textfield");
        filter.setInputPrompt("Filter papers...");
        //filter.addTextChangeListener(e -> refreshPapers(filter.getValue()));

        searchPaper = new MButton("Search");
        searchPaper.addStyleName(ValoTheme.BUTTON_PRIMARY);
        searchPaper.setIcon(FontAwesome.ARCHIVE);
        searchPaper.addClickListener(e -> searchPapers(filter.getValue()));

        newPaper = new MButton("New paper");
        newPaper.addStyleName(ValoTheme.BUTTON_PRIMARY);
        newPaper.setIcon(FontAwesome.PLUS_CIRCLE);
        newPaper.addClickListener(e -> paperForm.edit(new Paper()));

        paperForm = new PaperForm();
        paperForm.setSizeFull();

        HorizontalLayout mainLayout = new HorizontalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setWidth("100%");
        mainLayout.addComponent(filter);
        mainLayout.addComponent(newPaper);
        mainLayout.addComponent(searchPaper);
        mainLayout.addComponent(paperForm);
        mainLayout.setComponentAlignment(paperForm, Alignment.TOP_RIGHT); //TODO
        mainLayout.setComponentAlignment(searchPaper, Alignment.TOP_RIGHT);
        mainLayout.setComponentAlignment(newPaper, Alignment.TOP_RIGHT);
        mainLayout.setComponentAlignment(filter, Alignment.MIDDLE_LEFT);
        mainLayout.setExpandRatio(filter, 1);
        mainLayout.setExpandRatio(newPaper, 1);
        mainLayout.setStyleName("top-bar");

        return mainLayout;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        //crudLogic.enter(event.getParameters());
    }

    protected void setPaperListEnabled(boolean state) {
        paperList.setEnabled(state); //TODO probably it's not necessary
    }
    protected void setNewPaperEnabled(boolean state) {
        newPaper.setEnabled(state);
    }

    protected void setIt() {
        paperList.setContainerDataSource(new BeanItemContainer<>(
                Paper.class, service.findAll("")));
    }

    protected void refreshPapers() {
        refreshPapers(filter.getValue());

    }

    private void searchPapers(String s) {
        paperList.setContainerDataSource(new BeanItemContainer<>(Paper.class, service.search(s)));
    }

    private void refreshPapers(String stringFilter) {
        paperList.setContainerDataSource(new BeanItemContainer<>(
                Paper.class, service.findAll(stringFilter)));

        if (paperForm != null) {
            paperForm.setVisible(false);
        }
    }

}
