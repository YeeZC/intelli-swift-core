/**
 * Created by roy on 16/5/23.
 * 明细表样式
 */
BI.DetailTableSetting = BI.inherit(BI.Widget, {

    constant: {
        SINGLE_LINE_HEIGHT: 60,
        SIMPLE_H_GAP: 10,
        SIMPLE_L_GAP: 2,
        CHECKBOX_WIDTH: 16,
        EDITOR_WIDTH: 60,
        EDITOR_HEIGHT: 26,
        BUTTON_WIDTH: 40,
        BUTTON_HEIGHT: 30,
        ICON_WIDTH: 24,
        ICON_HEIGHT: 24
    },

    _defaultConfig: function () {
        return BI.extend(BI.DetailTableSetting.superclass._defaultConfig.apply(this, arguments), {
            baseCls: "bi-group-table-setting"
        })
    },

    _init: function () {
        BI.DetailTableSetting.superclass._init.apply(this, arguments);
        var self = this, o = this.options;
        //显示组件标题
        this.showTitle = BI.createWidget({
            type: "bi.multi_select_item",
            value: BI.i18nText("BI-Show_Chart_Title"),
            cls: "attr-names",
            logic: {
                dynamic: true
            }
        });
        this.showTitle.on(BI.Controller.EVENT_CHANGE, function () {
            self.widgetTitle.setVisible(this.isSelected());
            self.fireEvent(BI.GroupTableSetting.EVENT_CHANGE);
        });

        //组件标题
        this.title = BI.createWidget({
            type: "bi.sign_editor",
            cls: "title-input",
            width: 120
        });

        this.title.on(BI.SignEditor.EVENT_CHANGE, function () {
            self.fireEvent(BI.GroupTableSetting.EVENT_CHANGE)
        });

        //详细设置
        this.titleDetailSettting = BI.createWidget({
            type: "bi.show_title_detailed_setting_combo"
        });

        this.titleDetailSettting.on(BI.ShowTitleDetailedSettingCombo.EVENT_CHANGE, function () {
            self.fireEvent(BI.GroupTableSetting.EVENT_CHANGE)
        });

        this.widgetTitle = BI.createWidget({
            type: "bi.left",
            items: [this.title, this.titleDetailSettting],
            hgap: this.constant.SIMPLE_H_GAP
        });

        //组件背景
        this.widgetBackground = BI.createWidget({
            type: "bi.global_style_index_background"
        });
        this.widgetBackground.on(BI.GlobalStyleIndexBackground.EVENT_CHANGE, function () {
            self.fireEvent(BI.GroupTableSetting.EVENT_CHANGE);
        });

        var widgetTitle = BI.createWidget({
            type: "bi.left",
            cls: "single-line-settings",
            items: BI.createItems([{
                type: "bi.label",
                text: BI.i18nText("BI-Component_Widget"),
                cls: "line-title",
            }, {
                type: "bi.label",
                text: BI.i18nText("BI-Title"),
                cls: "line-title",
                lgap: 38
            }, {
                type: "bi.vertical_adapt",
                items: [this.showTitle]
            }, {
                type: "bi.vertical_adapt",
                items: [this.widgetTitle]
            }, {
                type: "bi.label",
                text: BI.i18nText("BI-Background"),
                cls: "line-title",
            },{
                type: "bi.vertical_adapt",
                items: [this.widgetBackground]
            }], {
                height: this.constant.SINGLE_LINE_HEIGHT
            }),
            hgap: this.constant.SIMPLE_H_GAP
        });

        //主题颜色
        this.colorSelector = BI.createWidget({
            type: "bi.color_chooser",
            width: this.constant.BUTTON_HEIGHT,
            height: this.constant.BUTTON_HEIGHT
        });
        this.colorSelector.on(BI.ColorChooser.EVENT_CHANGE, function () {
            self.fireEvent(BI.DetailTableSetting.EVENT_CHANGE);
        });
        //风格——1、2、3
        this.tableSyleGroup = BI.createWidget({
            type: "bi.button_group",
            items: BI.createItems(BICst.TABLE_STYLE_GROUP, {
                type: "bi.icon_button",
                extraCls: "table-style-font",
                width: this.constant.BUTTON_WIDTH,
                height: this.constant.BUTTON_HEIGHT,
                iconWidth: this.constant.ICON_WIDTH,
                iconHeight: this.constant.ICON_HEIGHT
            }),
            layouts: [{
                type: "bi.left"
            }]
        });
        this.tableSyleGroup.on(BI.ButtonGroup.EVENT_CHANGE, function () {
            self.fireEvent(BI.DetailTableSetting.EVENT_CHANGE);
        });
        var tableStyle = BI.createWidget({
            type: "bi.left_right_vertical_adapt",
            cls: "single-line-settings",
            items: {
                left: [{
                    type: "bi.label",
                    text: BI.i18nText("BI-Table_Sheet_Style"),
                    cls: "line-title"
                }, {
                    type: "bi.label",
                    text: BI.i18nText("BI-Theme_Color"),
                    cls: "attr-names"
                }, this.colorSelector, {
                    type: "bi.label",
                    text: BI.i18nText("BI-Table_Style"),
                    cls: "attr-names"
                }, this.tableSyleGroup]
            },
            height: this.constant.SINGLE_LINE_HEIGHT,
            lhgap: this.constant.SIMPLE_H_GAP
        });

        //显示序号
        this.showNumber = BI.createWidget({
            type: "bi.multi_select_item",
            value: BI.i18nText("BI-Display_Sequence_Number"),
            cls: "attr-names",
            logic: {
                dynamic: true
            }
        });
        this.showNumber.on(BI.Controller.EVENT_CHANGE, function () {
            self.fireEvent(BI.DetailTableSetting.EVENT_CHANGE);
        });
        var show = BI.createWidget({
            type: "bi.left",
            cls: "single-line-settings",
            items: [{
                type: "bi.label",
                text: BI.i18nText("BI-Element_Show"),
                cls: "line-title",
                height: this.constant.SINGLE_LINE_HEIGHT
            }, {
                type: "bi.vertical_adapt",
                items: [this.showNumber],
                height: this.constant.SINGLE_LINE_HEIGHT
            }],
            hgap: this.constant.SIMPLE_H_GAP
        });

        //冻结维度
        this.freezeFirstColumn = BI.createWidget({
            type: "bi.multi_select_item",
            value: BI.i18nText("BI-Freeze_FIRST_COLUMN"),
            cls: "attr-names",
            logic: {
                dynamic: true
            }
        });
        this.freezeFirstColumn.on(BI.Controller.EVENT_CHANGE, function () {
            self.fireEvent(BI.DetailTableSetting.EVENT_CHANGE);
        });

        var otherAttr = BI.createWidget({
            type: "bi.left",
            cls: "single-line-settings",
            items: [{
                type: "bi.label",
                text: BI.i18nText("BI-Interactive_Attr"),
                cls: "line-title",
                height: this.constant.SINGLE_LINE_HEIGHT
            }, {
                type: "bi.vertical_adapt",
                items: [this.freezeFirstColumn],
                height: this.constant.SINGLE_LINE_HEIGHT
            }],
            hgap: this.constant.SIMPLE_H_GAP
        });

        BI.createWidget({
            type: "bi.vertical",
            element: this.element,
            items: [widgetTitle, tableStyle, show, otherAttr],
            hgap: 10
        })
    },

    populate: function () {
        var wId = this.options.wId;
        this.showTitle.setSelected(BI.Utils.getWSShowNameByID(wId));
        this.widgetTitle.setVisible(BI.Utils.getWSShowNameByID(wId));
        this.title.setValue(BI.Utils.getWidgetNameByID(wId));
        this.titleDetailSettting.setValue(BI.Utils.getWSTitleDetailSettingByID(wId));
        this.widgetBackground.setValue(BI.Utils.getWSWidgetBGByID(wId));
        this.colorSelector.setValue(BI.Utils.getWSThemeColorByID(wId));
        this.tableSyleGroup.setValue(BI.Utils.getWSTableStyleByID(wId));
        this.showNumber.setSelected(BI.Utils.getWSShowNumberByID(wId));
        this.freezeFirstColumn.setSelected(BI.Utils.getWSFreezeFirstColumnById(wId));
    },

    getValue: function () {
        return {
            show_name: this.showTitle.isSelected(),
            widget_title: this.title.getValue(),
            title_detail: this.titleDetailSettting.getValue(),
            widget_bg: this.widgetBackground.getValue(),
            theme_color: this.colorSelector.getValue(),
            table_style: this.tableSyleGroup.getValue()[0],
            show_number: this.showNumber.isSelected(),
            freeze_first_column: this.freezeFirstColumn.isSelected()
        }
    },

    setValue: function (v) {
        this.showTitle.setSelected(v.show_name);
        this.title.setValue(v.widget_title);
        this.titleDetailSettting.setValue(v.title_detail);
        this.widgetBackground.setValue(v.widget_bg);
        this.colorSelector.setValue(v.theme_color);
        this.tableSyleGroup.setValue(v.table_style);
        this.showNumber.setSelected(v.show_number);
        this.freezeFirstColumn.setSelected(v.freeze_first_column);
    }
});
BI.DetailTableSetting.EVENT_CHANGE = "EVENT_CHANGE";
$.shortcut("bi.detail_table_setting", BI.DetailTableSetting);