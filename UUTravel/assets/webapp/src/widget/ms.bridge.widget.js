(function (window, undefined) {
    var _callbacks = {};
    var _scope;

    var _favStore;
    var _detailStore;

    var BridgeMethod = {
        getGeolocation: 'getGeolocation',
        showShare: 'showShare',
        showToast: 'showToast',
        confirm: 'confirm',
        resizeWindow: 'resizeWindow',
        openActivity: 'openActivity',
        checkUpdate: 'checkUpdate',
        closeActivity: 'closeActivity',

        addFav: "addFav",
        showWSShare: "showWSShare",

        addFavEx: "addFavEx",
        showWSShareEx: "showWSShareEx"
    };


    var SystemType = {
        ANDROID: 'ANDROID',
        IOS: 'IOS'
    };

    //调用OC方法的系协议
    var IosProtocol = 'objc://';
    //协议参数分隔符
    var IosUrlSpliter = '_/_';

    window.MsJsBridge = {
        init: function (opts) {
            'favStore' in opts && (_favStore = opts.favStore);
            'detailStore' in opts && (_detailStore = opts.detailStore)
        },
        addFav: function (data) {
            data = data || _detailStore.getValue()
            _favStore.add(data);

        },
        showWSShare: function (data) {
            var data = data || _detailStore.getValue();
            //调用分享
            window.MsJsBridge.showShare({
                title: data.place_name,
                content: data.place_intro,
                imgUrl: data.img,
                linkUrl: data.place_link
            });
        },
        //回调
        callback: function (event, data) {
            var opts = _callbacks[event];
            var jsonData = JSON.parse(data);
            if (event === BridgeMethod.confirm) {
                if (jsonData.success) {
                    if (jsonData.data === "Y") {
                        opts && opts.okCallback && opts.okCallback.call(opts.scope);
                    }
                    else {
                        opts && opts.cancelCallback && opts.cancelCallback.call(opts.scope);
                    }
                }
                else {
                    opts && opts.cancelCallback && opts.cancelCallback.call(opts.scope);
                }
                return;
            }

            if (BridgeMethod.addFavEx === event) {

                try {
                    window.MsJsBridge.addFav(jsonData.data);
                    window.MsJsBridge.showToast('收藏成功');
                }
                catch (e) {
                    window.MsJsBridge.showToast('收藏失败');
                }
                return;
            }

            if (event === BridgeMethod.showWSShareEx) {
                try {
                    window.MsJsBridge.showWSShare(jsonData.data);
                }
                catch (e) {
                }
                return;
            }



            if (event == BridgeMethod.checkUpdate) {
                window.MsJsBridge.checkUpdateCallback(jsonData, opts);
                return;
            }

            if (event === BridgeMethod.resizeWindow) {
                $ && $(window).resize();
                return;
            }

            if (event === BridgeMethod.showToast) {
                opts &&  opts.callback && opts.callback.call(opts.scope);
                return;
            }

            if (event === BridgeMethod.addFav) {
                try {
                    window.MsJsBridge.addFav();
                    window.MsJsBridge.showToast('收藏成功');
                }
                catch (e) {
                    window.MsJsBridge.showToast('收藏失败');
                }
                return;
            }

            if (event === BridgeMethod.showWSShare) {
                try {
                    window.MsJsBridge.showWSShare();
                }
                catch (e) {
                }
                return;
            }


            if (jsonData.success) {
                opts && opts.callback && opts.callback.call(opts.scope, jsonData.data);
            }
            else {
                opts && opts.errorCallback && opts.errorCallback.call(opts.scope, jsonData.data);
            }
        },
        //检查更新opts:{successCallback,errorCallback}
        checkUpdate: function (opts) {
            var sysType = window.MsJsBridge.getSytemTpye();
            _scope = opts.scope;
            _callbacks[BridgeMethod.checkUpdate] = opts;
            if (window.MsJsBridge.isReady(BridgeMethod.checkUpdate)) {
                if (sysType == SystemType.ANDROID) {
                    window.MsAndroidBridge[BridgeMethod.checkUpdate]();
                }
                else {
                    window.location.href = window
                                           .MsJsBridge
                                           .createIosUrl([BridgeMethod.checkUpdate]);
                }
            }
            else {
                opts && opts.errorCallback && opts.errorCallback.call(opts.scope);
            }
        },
        //checkUpdateCallback
        checkUpdateCallback: function (data, opts) {
            if (data.success) {
                if (data.data == "YES") {
                    opts && opts.successCallback.call(opts.scope);
                }
                else {
                    opts && opts.errorCallback.call(opts.scope);
                }
            }
            else {
                opts && opts.errorCallback.call(opts.scope);
            }
        },
        //获取地理位置信息,opts={callback:fn,errorCallback:fn,scope:object}
        getGeolocation: function (opts) {
            var sysType = window.MsJsBridge.getSytemTpye();
            _scope = opts.scope;
            _callbacks[BridgeMethod.getGeolocation] = opts;
            if (window.MsJsBridge.isReady(BridgeMethod.getGeolocation)) {
                if (sysType == SystemType.ANDROID) {
                    window.MsAndroidBridge[BridgeMethod.getGeolocation]();
                }
                else {
                    window.location.href = window
                                           .MsJsBridge
                                           .createIosUrl(['getGeolocation']);
                }
            }
            else {
                opts && opts.errorCallback && opts.errorCallback.call(opts.scope);
            }
        },
        //分享，opts={title:string,content:string,imgUrl:string,linkUrl:string}
        showShare: function (opts) {
            var sysType = window.MsJsBridge.getSytemTpye();
            _callbacks[BridgeMethod.showShare] = opts;
            if (window.MsJsBridge.isReady(BridgeMethod.showShare)) {
                if (sysType == SystemType.ANDROID) {
                    window.MsAndroidBridge[BridgeMethod.showShare](opts.title, opts.content, opts.imgUrl, opts.linkUrl);
                }
                else {
                    window.location.href = window
                                           .MsJsBridge
                                           .createIosUrl([
                                                'showShare',
                                                opts.title,
                                                opts.content,
                                                opts.imgUrl,
                                                opts.linkUrl]);
                }
            }
            else {
                opts && opts.errorCallback && opts.errorCallback.call(opts.scope);
            }
        },
        //显示对话框
        showToast: function (msg, closeCallback, scope) {
            var sysType = window.MsJsBridge.getSytemTpye();
            _callbacks[BridgeMethod.showToast] = { callback: closeCallback, scope: scope };
            if (window.MsJsBridge.isReady(BridgeMethod.showToast)) {
                if (sysType == SystemType.ANDROID) {
                    closeCallback && closeCallback.call(scope);
                    window.MsAndroidBridge[BridgeMethod.showToast](msg, 'CENTER');
                }
                else {
                    window.location.href = window
                                          .MsJsBridge
                                          .createIosUrl(['showToast', msg]);
                }
            }
        },
        //确认取消：opts={msg,title,okCallback,cancelCallback,scope}
        confirm: function (opts) {
            var sysType = window.MsJsBridge.getSytemTpye();
            _callbacks[BridgeMethod.confirm] = opts;
            if (window.MsJsBridge.isReady(BridgeMethod.confirm)) {
                if (sysType == SystemType.ANDROID) {
                    window.MsAndroidBridge[BridgeMethod.confirm](opts.msg, opts.title);
                }
                else {
                    window.location.href = window
                                            .MsJsBridge
                                            .createIosUrl(['confirm', opts.title, opts.msg]);
                }
            }
        },
        //启动指定url的activity
        openActivity: function (opts) {
            var sysType = window.MsJsBridge.getSytemTpye();
            _callbacks[BridgeMethod.openActivity] = opts;
            if (window.MsJsBridge.isReady(BridgeMethod.openActivity)) {
                if (sysType == SystemType.ANDROID) {
                    window.MsAndroidBridge[BridgeMethod.openActivity](opts.url);
                }
                else {
                    //window.location.href = window
                    //                        .MsJsBridge
                    //                        .createIosUrl(['confirm', opts.title, opts.msg]);
                }
            }
        },
        //关闭当前native界面
        closeActivity: function () {
            var sysType = window.MsJsBridge.getSytemTpye();
            _callbacks[BridgeMethod.closeActivity] = opts;
            if (window.MsJsBridge.isReady(BridgeMethod.closeActivity)) {
                if (sysType == SystemType.ANDROID) {
                    window.MsAndroidBridge[BridgeMethod.closeActivity]();
                }
                else {
                    //window.location.href = window
                    //                        .MsJsBridge
                    //                        .createIosUrl(['confirm', opts.title, opts.msg]);
                }
            }
        },
        //android 桥接对象是否已准备
        isReady: function (fun) {
            var systemType = window.MsJsBridge.getSytemTpye();
            if (systemType == SystemType.ANDROID) {
                return window.MsAndroidBridge && (fun in window.MsAndroidBridge);
            }
            else {
                return true;
            }
        },
        //判断桥接对象是否有效
        isAvailable: function () {
            var systemType = window.MsJsBridge.getSytemTpye();
            if (systemType == SystemType.ANDROID) {
                return !!window.MsAndroidBridge;
            }
            else {
                return true;
            }
        },
        //获取当前应用所属类型
        getSytemTpye: function () {
            if (window.navigator.userAgent.indexOf('MsWebviewAndroid') > -1) {
                return SystemType.ANDROID;
            }

            if (window.navigator.userAgent.indexOf('MsWebviewIOS') > -1) {
                return SystemType.IOS;
            }
        },
        //生成OC调用URL
        createIosUrl: function (params) {
            params = params || [];
            for (var i = 0, l = params.length; i < l; i += 1) {
                params[i] = encodeURIComponent(params[i]);
            }
            return IosProtocol + params.join(IosUrlSpliter);
        },
        hybridCallback: function (opts) {
            if (window.navigator.userAgent.indexOf('MsWebviewAndroid') > -1) {
                opts && opts.android && opts.android();
            }
            else if (window.navigator.userAgent.indexOf('MsWebviewIOS') > -1) {
                opts && opts.ios && opts.ios();
            }
            else {
                if (window.navigator.userAgent.toLowerCase().indexOf('micromessenger') > -1) {
                    opts && opts.weixin && opts.weixin();
                }
                else {
                    opts && opts.web && opts.web();
                    if (/(iPhone|iPad|iPod|iOS)/i.test(window.navigator.userAgent)) {
                        opts && opts.iosWeb && opts.iosWeb();
                    } else if (/(Android)/i.test(window.navigator.userAgent)) {
                        opts && opts.androidWeb && opts.androidWeb();
                    }
                }
            }
        }

    };

})(window);