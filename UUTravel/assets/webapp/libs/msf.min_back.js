/**
 * @license RequireJS text 2.0.12 Copyright (c) 2010-2014, The Dojo Foundation All Rights Reserved.
 * Available via the MIT or new BSD license.
 * see: http://github.com/requirejs/text for details
 */

define("MsCore",[],function(){var e=[].slice,t=function(){};return t.Class=function(){function r(){this.__propertys__(),this.initialize.apply(this,arguments)}if(arguments.length==0||arguments.length>2)throw"参数错误";var t=null,n=e.call(arguments);typeof n[0]=="function"&&(t=n.shift()),n=n[0],r.superclass=t,r.subclasses=[];var i=function(){},s=n.__propertys__||function(){};if(t){t.prototype.__propertys__&&(i=t.prototype.__propertys__);var o=function(){};o.prototype=t.prototype,r.prototype=new o,t.subclasses.push(r)}var u=r.superclass&&r.superclass.prototype;for(var a in n){var f=n[a];if(u&&typeof f=="function"){var l=/^\s*function\s*\(([^\(\)]*?)\)\s*?\{/i.exec(f.toString())[1].replace(/\s/i,"").split(",");l[0]==="$super"&&u[a]&&(f=function(t,n){return function(){var r=this,i=[function(){return u[t].apply(r,arguments)}];return n.apply(this,i.concat(e.call(arguments)))}}(a,f))}r.prototype[a]=f}r.prototype.initialize||(r.prototype.initialize=function(e){if(this.overrideArgs)for(var t in e)this[t]=e[t]});for(var c in t)t.hasOwnProperty(c)&&c!=="prototype"&&c!=="superclass"&&(r[c]=t[c]);return r.prototype.constructor=r,r},t}),define("MsApp",["MsCore"],function(e){var t={__propertys__:function(){this.overrideArgs=!0,this.controllers={},this.executing=!1,this.preControllerName=""},initialize:function(e){for(var t in e)this[t]=e[t];this.contrllerPath||(this.controllerPath="controllers"),this.viewPath||(this.viewPath="views");if(window.location.hash){var n=window.location.hash.match(/[#]?([^?]*)/);n&&n.length==2&&(this.defaultController=window.location.hash)}if(!this.basePath)throw new Error("App没有设置BasePath");this.initPage=!1,this.request={},this.activeController=null},startup:function(){var e=this;$.mobile.appendViewHtml=function(t){var n="";t.indexOf("#")>0&&(n=t.split("#")[1]),e._execute(e._getOptions(n))},this._setRequest(),this._execute(this._getOptions());var e=this;$(document.body).on("pagecontainerbeforeshow",function(t,n){if(e.executing)return;e.executing=!0;if(n.prevPage.length>0){var r=e.controllers[n.prevPage.data("controllerName")];e.preControllerName=n.prevPage.data("controllerName"),r&&r._hide()}if(n.toPage.length>0){var i=e.controllers[n.toPage.data("controllerName")];i&&i.handlePage(n.toPage),i&&i._beforeShow()}}),$(document.body).on("pagecontainershow",function(t,n){e.renderCompleted=!0,e.executing=!1,e.switchCompleted();if(n.toPage.length>0){var r=n.toPage.data("controllerName"),i=e.controllers[r];i&&(i.isActive=!0,i.onShow&&i.onShow(),e.activeController=i)}}),$(document).scroll(function(){var t=e.activeController.onScrollOpts;if(!e.activeController.isActive||!e.renderCompleted)return;$(document).scrollTop()<=0&&t.topCallback&&t.topCallback(),$(document).scrollTop()>=$(document).height()-$(window).height()&&t.bottomCallback&&(console.log(t),t.bottomCallback())})},_getHashName:function(e){var t=e||window.location.hash,n,r=/[#]?([^?]*)/,i=t.match(r);if(i&&i.length===2)return i[1]},_getOptions:function(e){e=e||window.location.hash;var t=this._getHashName(e),n,r;return t?(n=t,r=e):(n=this._getHashName(this.defaultController),r="#"+this.defaultController),{url:r,controllerName:n}},_execute:function(e,t){if(this.executing)return;this.executing=!0;var n,r,i=this;e.controllerName||(e.controllerName=this.defaultController),r=this.controllers[e.controllerName];if(r){i.executing=!1,r.url=e.url,r.pageRender(),this.activeController=r;return}n=e.controllerName+"Controller",requirejs([n],function(t){i.executing=!1;if(!t)throw new Error("未找到对应页面的controller对象");var n=new t({controllerName:e.controllerName,url:e.url,app:i});i.controllers[e.controllerName]=n,i.activeController=n,n._create()})},jump:function(e,t){this._execute(this._getOptions(e),t)},"goto":function(e){this._execute(this._getOptions(e),!0)},_onhashChange:function(){},_setRequest:function(){var e=window.location.hash,t=e.indexOf("?")>-1?e.substring(e.indexOf("?")+1,e.length):null,n,r,i,s;this.request={};if(t){n=t.split("&");for(r=0,i=n.length;r<i;r+=1)s=n[r].split("="),s&&s.length===2&&(this.request[s[0]]=s[1])}},back:function(){if(this.backing)return;this.backing=!0,$.mobile.back()},switchCompleted:function(){this.backing=!1}};return new e.Class(t)});var __SERVER_DATE_TIME__;define("MsUtil",[],function(){var e={dateTime:{getTime:function(){return(new Date).getTime()}},date:{formatNetString:function(e){if(e&&e.split("(").length>1){var t=new RegExp("\\/Date\\(([-+])?(\\d+)(?:[+-]\\d{4})?\\)\\/"),n=(e||"").match(t),r=n?new Date(((n[1]||"")+n[2])*1):null;if(r)return this.parseDateTime(r)}return e},parseDateTime:function(e){var t=function(e){return e<10?"0"+e:e};return t(e.getFullYear())+"-"+t(e.getMonth()+1)+"-"+t(e.getDate())+" "+t(e.getHours())+":"+t(e.getMinutes())+":"+t(e.getSeconds())}},md5:function(e){return $.md5(e)}};return e}),define("MsStore",["MsCore","MsUtil"],function(e,t){var n=/[1-9]\d*(H|D|M)/i,r=/([1-9]\d*)(H|D|M)/i,i={HOUR:"H",MINUTE:"M",DAY:"D"},s={__propertys__:function(){this.key="",this.defaultData,this.overrideArgs=!0,this.value,this.JsonValue,this.cacheTime,this.proxy},initialize:function(){this.proxy=window.localStorage,this._assert()},setValue:function(e){this._setLocalStorageData(e)},getValue:function(){var e,t;t=this._getLocalStorageData();if(t){e=JSON.parse(t);if(e&&!this._isExpired(e.date))return e.val}return null},_assert:function(){if(!this.proxy)throw new Error("当前环境不支持localStorage");if(!this.cacheTime||!n.test(this.cacheTime))throw new Error("过期时间设置不正确，格式：1D或1H或1M");if(!this.key)throw new Error("请设置Store key值")},_getLocalStorageData:function(){return this.proxy[this.key]},_setLocalStorageData:function(e){this.proxy[this.key]=JSON.stringify(this._package(e))},_package:function(e){return{val:e,date:t.dateTime.getTime()/1e3}},_isExpired:function(e){var s,o,u,a,f,l;if(!this.cacheTime||!n.test(this.cacheTime))return!0;s=this.cacheTime.match(r);if(s.length!==3)return!0;o=+s[1],u=s[2];switch(u){case i.MINUTE:a=60*o;break;case i.HOUR:a=3600*o;break;case i.DAY:a=86400*o}return f=t.dateTime.getTime()/1e3,l=f-e,a&&l>0&&a-l>0?!1:!0}},o=new e.Class(s);return o.getInstance=function(){return this.instance||(this.instance=new this),this.instance},o}),define("MsModel",["MsCore"],function(e){var t={__propertys__:function(){this.protocol="http",this.method="POST",this.param={},this.url="",this.path="",this.useCache,this.async=!0,this.timeout=1e4,this.ajax=$.ajax,this.result,this.proxy,this.userDataStore={},this.ver="1.0",this.source="MS"},initialize:function($super,e){this._assert()},_assert:function(){if(!/^http[s]*$/i.test(this.protocol))throw new Error("AJAX协议必须为http或者https");if(!/^post|delete|get|put|options$/i.test(this.method))throw new Error("AJAX的方法设置不正确");if(!this.url)throw new Error("AJAX的URL设置不正确")},getParam:function(){var e=this.userDataStore?this.userDataStore.getValue():null;return{header:{auth:e?e.authToken?e.authToken:"":"",ver:this.ver,source:this.source,act:this.path},body:this.param}},execute:function(e){if(this._isUseCache()){var t=this.result.getValue();if(t){e.onSuccess&&e.onSuccess(t);return}}this.proxy=this.ajax(this._getOptions(e))},abort:function(){this.proxy&&this.proxy.abort&&this.proxy.abort()},_setCache:function(e){this._isUseCache()&&this.result.setValue(e)},_isUseCache:function(){return this.useCache&&this.result&&"getValue"in this.result?!0:!1},_getOptions:function(e){var t=this;return{type:this.method,url:this._getUrl(),dataType:"JSON",contentType:"application/json;charset=utf-8",data:JSON.stringify(this.getParam()),beforeSend:function(e){e.setRequestHeader("Access-Control-Allow-Origin","*"),e.setRequestHeader("Access-Control-Allow-Methods","POST,OPTIONS")},success:function(n){n=t._validResponse(n);if(!n.isSuccess){e.onError&&e.onError(n);return}t._isUseCache()&&t.result.setValue(n.body),e.onSuccess&&e.onSuccess(n.body)},error:function(t){"isSuccess"in t||(t={message:"发生未知错误",isSuccess:!1,code:"J02"}),e.onError&&e.onError(t)},complete:function(){e.onComplete&&e.onComplete()},abort:function(){e.abort&&e.abort()},async:this.async,timeout:this.timeout}},_getUrl:function(){return this.protocol+"://"+this.url+this.path},_validResponse:function(e){return"isSuccess"in e?e:{message:"发生未知错误",isSuccess:!1,code:"J01"}}},n=new e.Class(t);return n.getInstance=function(){return this.instance||(this.instance=new this),this.instance},n}),define("MsWidgetBase",["MsCore"],function(e){"use strict";var t={__propertys__:function(){this.isCreated=!1,this.widgetType="Default"},initialize:function(e){for(var t in e)this[t]=e[t]},getWidgetId:function(){return"MS-WIDGET-"+this.widgetType+"-"+(new Date).getTime()}};return new e.Class(t)}),define("MsWidgetMask",["MsCore","MsWidgetBase"],function(e,t){"use strict";var n={__propertys__:function(){this.isCreated=!1,this.widgetType="MASK"},initialize:function($super,e){$super(e)},create:function(){this.isCreated||(this.isCreated=!0,this.root=$('<div class="ms-widget-mask"><div></div></div>'),$(document.body).append(this.root),this.root.attr("id",this.getWidgetId()))},show:function(e){var t=this;this.create(),this.root.css({height:$(document).height()}),this.root.on("touchstart touchmove touchend",function(e){e.preventDefault()}),this.root.show(),setTimeout($.proxy(function(){e&&this.root.one("tap",function(t){e()})},this),500)},hide:function(){this.root&&this.root.hide()}},r=new e.Class(t,n);return r.create=function(){return this.instance||(this.instance=new this),this.instance},r}),define("MsWidgetLoading",["MsCore","MsWidgetBase","MsWidgetMask"],function(e,t,n){"use strict";var r=n.create(),i={__propertys__:function(){this.isCreated=!1,this.widgetType="LOADING"},initialize:function($super,e){$super(e)},_injectHTML:function(){var e=['<div class="ms-loading-widget-container" style="display:none">','<div class="ms-loading-widget ms-widget" ">','<div class="loading-container ms-loading"></div>','<div class="loading-logo ms-logo"></div>',"</div>","</div>"];this.widgetRoot=$(e.join("")),$(document.body).append(this.widgetRoot),this.widgetRoot.attr("id",this.getWidgetId()),this.widgetRoot.on("touchstart touchmove touchend",function(e){e.preventDefault()})},show:function(e){e=e||{},this.isCreated||(this.isCreated=!0,this._injectHTML()),e.noMask||r.show(),this.widgetRoot.show(),e.showCB&&e.showCB(this)},hide:function(){if(!this.isCreated)return;r&&r.hide(),this.widgetRoot&&this.widgetRoot.hide()}},s=new e.Class(t,i);return s.create=function(){return this.instance||(this.instance=new this),this.instance},s}),define("MsWidgetMessage",["MsCore","MsWidgetBase","MsWidgetMask"],function(e,t,n){"use strict";var r=n.create(),i={__propertys__:function(){this.isCreated=!1,this.widgetType="MESSAGE",this.defaultOptions={showTitle:!1,showConfirm:!1,title:"提示",content:"提示信息"},this.isCallback=!1},initialize:function($super,e){$super(e);for(var t in e)this[t]=e[t]},_injectHTML:function(){var e=['<div class="pop">','<p class="pop_title" style="display:none">title</p>','<div class="pop_body">','<div class="pop_content" >content</div>','<div class="pop_btn tc" style="display:none">','<input type="button" data-role="none" name="name" value="取消" class="pop_btn_cancel" />','<input type="button" data-role="none" name="name" value="确认" class="pop_btn_ok" /></div>',"</div>","</div>"];this.widgetRoot=$(e.join("")),$(document.body).append(this.widgetRoot),this.widgetRoot.attr("id",this.getWidgetId()),$(window).on("resize",$.proxy(this._resize,this))},_create:function(){this.isCreated||(this.isCreated=!0,this._injectHTML())},show:function(e){this._create();var t={};for(var n in this.defaultOptions)t[n]=this.defaultOptions[n];for(var n in e)t[n]=e[n];var i=this.widgetRoot.find(".pop_title"),s=this.widgetRoot.find(".pop_content"),o=this.widgetRoot.find(".pop_btn"),u=this.widgetRoot.find(".pop_btn_ok"),a=this.widgetRoot.find(".pop_btn_cancel"),f=this;t.showTitle?i.show():i.hide(),i.html(t.title),s.html(t.content),t.showConfirm?o.show():o.hide(),this.isCallback=!1,t.showConfirm?(t.confirmCallback&&u.one("tap",function(){t.confirmCallback(),f.hide()}),t.cancelCallback&&a.one("tap",function(){t.cancelCallback(),f.hide()}),r.show()):(r.show($.proxy(function(){this.isCallback||(this.isCallback=!0,this.hide(),t.closeCallback&&t.closeCallback())},this)),t.delayClose&&setTimeout($.proxy(function(){this.isCallback||(this.isCallback=!0,this.hide(),t.closeCallback&&t.closeCallback())},this),t.delay||5e3)),this.widgetRoot.show(),this._setStyle()},hide:function(){r.hide(),this.widgetRoot.hide()},_setStyle:function(){var e=this.widgetRoot.width(),t=$(window).width(),n=(t-e)/2;this.widgetRoot.css({left:n})},_resize:function(){this._setStyle()}},s=new e.Class(t,i);return s.create=function(){return this.instance||(this.instance=new this),this.instance},s}),define("MsController",["MsCore","MsWidgetLoading","MsWidgetMessage"],function(e,t,n){var r=t.create(),i=n.create(),s={__propertys__:function($super){this.controllerName="",this.root,this.contentRoot,this.created=!1,this.events={},this.changeHash=!0,this.title=""},initialize:function(e){for(var t in e)this[t]=e[t];this.request=this.app.request,this.isActive=!1,this.isShowLoading=!1,this.onScrollOpts={}},onCreate:function(){},_create:function(){var e=this;this.setTitle(this.title?this.title:""),this.root.attr("id",this.controllerName),this.root.data("controllerName",this.controllerName),$(document.body).append(this.root),FastClick.attach(this.root.get()[0]),this.app.initPage?(this.contentRoot=this.root.find(".ms-content"),this.pageInit(),this.pageRender()):($.mobile.initializePage(),this.app.initPage=!0,this.contentRoot=this.root.find(".ms-content"),this.pageInit(),this.pageRender(!0))},onShow:function(){},onBeforeShow:function(){},onHide:function(){},_show:function(){this.onBeforeShow(),$(document.body).pagecontainer("change",this.root,{role:"page",changeHash:this.changeHash}),this._setRequest(),this.setTitle(this.title?this.title:""),this.isActive=!0},_start:function(e){e&&this._show(),this.onCreate(),this.onBeforeShow()},_onlyShow:function(){$(document.body).pagecontainer("change",this.root,{role:"page",dataUrl:this.url,changeHash:this.changeHash}),this._setRequest()},setRequest:function(){this.app._setRequest.call(this.app),this.request=this.app.request},_hide:function(){this.isActive=!1,this.onHide()},onDispose:function(){},_beforeShow:function(){this.setTitle(this.title?this.title:""),this.isActive=!0,this._setRequest(),this.onBeforeShow(),this.setPageId(),this.setUid(),"UBT"in window&&window.UBT!==undefined&&UBT&&UBT.traceStart&&UBT.traceStart()},_showIndex:function(){this.root=$("#index"),this.contentRoot=this.root.find(".page-content"),this.onCreate()},injectHTML:function(e){this.root=$(e)},onBeforeJump:function(){},jump:function(e,t){this.onBeforeJump(),this.app.jump(e,t),this.onHide(),this.isActive=!1},"goto":function(e){this.app.goto(e)},onBeforeBack:function(){},back:function(){this.onBeforeBack(),this.onHide(),this.isActive=!1,this.app.back()},showLoading:function(e){this.isShowLoading=!0,r.show(e)},hideLoading:function(){this.isShowLoading=!1,r.hide()},showMessage:function(e){i.show(e)},hideMessage:function(){i.hide()},template:function(e){return _.template(e)},handlePage:function(e){},scroll:function(e){this.onScrollOpts=e||{}},setTitle:function(e){document.title=e},_setRequest:function(){var e=window.location.hash,t=e.indexOf("?")>-1?e.substring(e.indexOf("?")+1,e.length):null,n,r,i,s;this.request={};if(t){n=t.split("&");for(r=0,i=n.length;r<i;r+=1)s=n[r].split("="),s&&s.length===2&&(this.request[s[0]]=s[1])}},pageInit:function(){this.onCreate()},pageRender:function(e){if(this.isActive)return;this.app.renderCompleted=!1,this.isActive=!0,e||$(document.body).pagecontainer("change",this.root,{role:"page",dataUrl:this.url,changeHash:this.changeHash})},getPreControllerName:function(){return this.app.preControllerName},makeQrcode:function(e){var t={container:"",text:"",width:128,height:128,colorDark:"#000000",colorLight:"#ffffff",correctLevel:QRCode.CorrectLevel.H};t=$.extend(t,e);if(!t.container)throw new Error("二维码生成容器为空");this.qrcodes||(this.qrcodes={}),this.qrcodes[t.container]?this.qrcodes[t.container].makeCode(t.text):this.qrcodes[t.container]=new QRCode(t.container,t);var n=this,r=setTimeout(function(){n.root.find("#"+t.container).find("img").css("display",""),r=null},100)},setPageId:function(e){var t=$("#ubt-pid");e=e||this.pageId,t&&e&&t.val(e)},setUid:function(e){var t=$("#ubt-uid");e=e||this.uid,t&&e&&t.val(e)}};return new e.Class(s)}),define("MsEnvt",[],function(){"use strict";var e=30,t=1,n=!1,r={execute:function(e){this.isInApp()&&e.app?e.app():e.wx?this.directWeiXin(e.wx):e.wxshare?this.directWeiXin(function(){WeixinJSBridge.on("menu:share:timeline",function(t){var n={appid:"wxe103af465b0e3bf0",title:e.wxshare.title,link:e.wxshare.link,img_url:e.wxshare.imgUrl,desc:e.wxshare.desc,img_width:80,img_height:80};WeixinJSBridge.invoke("shareTimeline",n,function(e){})}),WeixinJSBridge.on("menu:share:appmessage",function(t){var n={appid:"wxe103af465b0e3bf0",title:e.wxshare.title,link:e.wxshare.link,img_url:e.wxshare.imgUrl,desc:e.wxshare.desc,img_width:80,img_height:80};WeixinJSBridge.invoke("sendAppMessage",n,function(e){})})}):e.web&&e.web()},isInApp:function(){return!1},isInWeb:function(){return!1},isInWeixin:function(){return n},directWeiXin:function(r){if(typeof window.WeixinJSBridge=="undefined"||typeof window.WeixinJSBridge.invoke=="undefined"){if(t>=e)return;setTimeout($.proxy(function(){this.directWeiXin(r)},this),500)}else r&&r(),n=!0;t++},getAppStatus:function(){if("appInfo"in window&&"status"in window.appInfo)return window.appInfo.status;throw new Error("未找到应用程序状态")},getAppInfo:function(){if("appInfo"in window)return window.appInfo;throw new Error("未找到应用程序信息")}};return r.directWeiXin(),r}),define("MsWidgetGeoLocation",[],function(){"use strict";var e={location:function(e,t){if(!navigator.geolocation||!navigator.geolocation.getCurrentPosition)return{code:-1};navigator.geolocation.getCurrentPosition(e,t,{enableHighAcuracy:!0,timeout:5e3,maximumAge:1e3})},showInBaiduMap:function(e,t){var n,r,i,s,o;n=e.coords.longitude,r=e.coords.latitude,i=new BMap.Map(t),s=new BMap.Point(n,r),i.centerAndZoom(s,15),o=new BMap.Marker(new BMap.Point(n,r)),i.addOverlay(o),i.addControl(new BMap.ZoomControl)}};return e}),define("MsWidgetDialog",[],function(){var e=['<div data-role="page" id="global-dialog-page" data-close-btn="right" >','<div data-role="header">',"<h2>Dialog</h2>","</div>",'<div role="main" class="ui-content">',"<p>I am a dialog</p>","</div>","</div>"],t,n="global-dialog-page",r={init:function(){if(!t){t=$("#"+n);if(!t||t.length<=0)t=$(e.join("")),$(document.body).append(t)}},show:function(e){e=e||{};var t,r,i="#"+n;this.init(),t=e.title||"提示",r=e.content||"",$.mobile.changePage(i,{role:"dialog"})},showConfirm:function(e){this.init()}};return r}),define("MsWidgetDateTime",[],function(){"use strict";var e={bindDate:function(e,t){var n=this.getConfig(),r=$.extend(n.date,n["default"],t);$(e).mobiscroll(r).date(r)},bindDatetime:function(e,t){var n=this.getConfig(),r=$.extend(n.datetime,n["default"],t);$(e).mobiscroll(r).datetime(r)},bindTime:function(e,t){var n=this.getConfig(),r=$.extend(n.time,n["default"],t);$(e).mobiscroll(r).time(r)},getConfig:function(){var e=(new Date).getFullYear(),t={};return t.date={preset:"date"},t.datetime={preset:"datetime"},t.time={preset:"time"},t.default={theme:"android-ics light",display:"modal",mode:"scroller",lang:"zh",startYear:e-10,endYear:e+10},t}};return e}),define("MsWidgetDropdown",[],function(){"use strict";var e={initialize:function($super,e){$super(e)},bind:function(e,t){var n={placehoder:"请选择",theme:"android-ics light",accent:" ",mode:"scroller",minWidth:200,showInput:!1,showLabel:!1};$(e).mobiscroll().select($.extend(n,t))},getValue:function(e){return $(e).mobiscroll("getValue")},setValue:function(e,t){var n=[];n.push(t),$(e).mobiscroll("setValue",t,!0)}};return e}),define("text",["module"],function(e){var t,n,r,i,s,o=["Msxml2.XMLHTTP","Microsoft.XMLHTTP","Msxml2.XMLHTTP.4.0"],u=/^\s*<\?xml(\s)+version=[\'\"](\d)*.(\d)*[\'\"](\s)*\?>/im,a=/<body[^>]*>\s*([\s\S]+)\s*<\/body>/im,f=typeof location!="undefined"&&location.href,l=f&&location.protocol&&location.protocol.replace(/\:/,""),c=f&&location.hostname,h=f&&(location.port||undefined),p={},d=e.config&&e.config()||{};t={version:"2.0.12",strip:function(e){if(e){e=e.replace(u,"");var t=e.match(a);t&&(e=t[1])}else e="";return e},jsEscape:function(e){return e.replace(/(['\\])/g,"\\$1").replace(/[\f]/g,"\\f").replace(/[\b]/g,"\\b").replace(/[\n]/g,"\\n").replace(/[\t]/g,"\\t").replace(/[\r]/g,"\\r").replace(/[\u2028]/g,"\\u2028").replace(/[\u2029]/g,"\\u2029")},createXhr:d.createXhr||function(){var e,t,n;if(typeof XMLHttpRequest!="undefined")return new XMLHttpRequest;if(typeof ActiveXObject!="undefined")for(t=0;t<3;t+=1){n=o[t];try{e=new ActiveXObject(n)}catch(r){}if(e){o=[n];break}}return e},parseName:function(e){var t,n,r,i=!1,s=e.indexOf("."),o=e.indexOf("./")===0||e.indexOf("../")===0;return s!==-1&&(!o||s>1)?(t=e.substring(0,s),n=e.substring(s+1,e.length)):t=e,r=n||t,s=r.indexOf("!"),s!==-1&&(i=r.substring(s+1)==="strip",r=r.substring(0,s),n?n=r:t=r),{moduleName:t,ext:n,strip:i}},xdRegExp:/^((\w+)\:)?\/\/([^\/\\]+)/,useXhr:function(e,n,r,i){var s,o,u,a=t.xdRegExp.exec(e);return a?(s=a[2],o=a[3],o=o.split(":"),u=o[1],o=o[0],(!s||s===n)&&(!o||o.toLowerCase()===r.toLowerCase())&&(!u&&!o||u===i)):!0},finishLoad:function(e,n,r,i){r=n?t.strip(r):r,d.isBuild&&(p[e]=r),i(r)},load:function(e,n,r,i){if(i&&i.isBuild&&!i.inlineText){r();return}d.isBuild=i&&i.isBuild;var s=t.parseName(e),o=s.moduleName+(s.ext?"."+s.ext:""),u=n.toUrl(o),a=d.useXhr||t.useXhr;if(u.indexOf("empty:")===0){r();return}!f||a(u,l,c,h)?t.get(u,function(n){t.finishLoad(e,s.strip,n,r)},function(e){r.error&&r.error(e)}):n([o],function(e){t.finishLoad(s.moduleName+"."+s.ext,s.strip,e,r)})},write:function(e,n,r,i){if(p.hasOwnProperty(n)){var s=t.jsEscape(p[n]);r.asModule(e+"!"+n,"define(function () { return '"+s+"';});\n")}},writeFile:function(e,n,r,i,s){var o=t.parseName(n),u=o.ext?"."+o.ext:"",a=o.moduleName+u,f=r.toUrl(o.moduleName+u)+".js";t.load(a,r,function(n){var r=function(e){return i(f,e)};r.asModule=function(e,t){return i.asModule(e,f,t)},t.write(e,a,r,s)},s)}};if(d.env==="node"||!d.env&&typeof process!="undefined"&&process.versions&&!!process.versions.node&&!process.versions["node-webkit"])n=require.nodeRequire("fs"),t.get=function(e,t,r){try{var i=n.readFileSync(e,"utf8");i.indexOf("﻿")===0&&(i=i.substring(1)),t(i)}catch(s){r&&r(s)}};else if(d.env==="xhr"||!d.env&&t.createXhr())t.get=function(e,n,r,i){var s=t.createXhr(),o;s.open("GET",e,!0);if(i)for(o in i)i.hasOwnProperty(o)&&s.setRequestHeader(o.toLowerCase(),i[o]);d.onXhr&&d.onXhr(s,e),s.onreadystatechange=function(t){var i,o;s.readyState===4&&(i=s.status||0,i>399&&i<600?(o=new Error(e+" HTTP status: "+i),o.xhr=s,r&&r(o)):n(s.responseText),d.onXhrComplete&&d.onXhrComplete(s,e))},s.send(null)};else if(d.env==="rhino"||!d.env&&typeof Packages!="undefined"&&typeof java!="undefined")t.get=function(e,t){var n,r,i="utf-8",s=new java.io.File(e),o=java.lang.System.getProperty("line.separator"),u=new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(s),i)),a="";try{n=new java.lang.StringBuffer,r=u.readLine(),r&&r.length()&&r.charAt(0)===65279&&(r=r.substring(1)),r!==null&&n.append(r);while((r=u.readLine())!==null)n.append(o),n.append(r);a=String(n.toString())}finally{u.close()}t(a)};else if(d.env==="xpconnect"||!d.env&&typeof Components!="undefined"&&Components.classes&&Components.interfaces)r=Components.classes,i=Components.interfaces,Components.utils["import"]("resource://gre/modules/FileUtils.jsm"),s="@mozilla.org/windows-registry-key;1"in r,t.get=function(e,t){var n,o,u,a={};s&&(e=e.replace(/\//g,"\\")),u=new FileUtils.File(e);try{n=r["@mozilla.org/network/file-input-stream;1"].createInstance(i.nsIFileInputStream),n.init(u,1,0,!1),o=r["@mozilla.org/intl/converter-input-stream;1"].createInstance(i.nsIConverterInputStream),o.init(n,"utf-8",n.available(),i.nsIConverterInputStream.DEFAULT_REPLACEMENT_CHARACTER),o.readString(n.available(),a),o.close(),n.close(),t(a.value)}catch(f){throw new Error((u&&u.path||"")+": "+f)}};return t}),define("text!MsWidgetRegionHtml",[],function(){return'<div data-role="page" id="ms_widget_select" data-theme="temporary">\r\n    <div class="heaeder" data-position="fixed" data-fullscreen="true" data-hide-during-focus="none" data-tap-toggle="false" data-rol="header">\r\n        <div class="header_box">\r\n            <a href="javascript:void(0);" class="header_back">返回上一页</a>\r\n            <h3 class="header_title">集合地点</h3>\r\n            <div class="header_sort">\r\n               \r\n            </div>\r\n        </div>\r\n    </div>\r\n    <div class="content ms-content ui-content_ptop" data-role="content" >\r\n\r\n        <div class="registration_box">\r\n\r\n            <div class="gathering_place">\r\n                <ul class="job_categories gathering_place_province" id="region_province">\r\n  \r\n                </ul>\r\n                <ul class="job_categories gathering_place_city" id="region_city">\r\n                   \r\n                </ul>\r\n                <ul class="job_categories gathering_place_district" id="region_district">\r\n                    \r\n                </ul>\r\n            </div>\r\n\r\n        </div>\r\n    </div>\r\n</div>\r\n'}),define("MsWidgetRegion",["MsCore","MsWidgetBase","text!MsWidgetRegionHtml"],function(e,t,n){"use strict";var r={},i={__propertys__:function(){this.isCreated=!1,this.widgetType="REGION",this.root={},this.selectValue={}},initialize:function(e){for(var t in e)this[t]=e[t];this.regions=r},injectHTML:function(e){$(document.body).append(e)},onCreate:function(){var e=this;this.isCreated||(this.isCreated=!0,this.root=$(n),this.root.attr("id",this.getWidgetId()),this.injectHTML(this.root),this.root.find(".header_back").on("tap",function(){e.hide()}),this.regionEles={province:this.root.find("#region_province"),city:this.root.find("#region_city"),district:this.root.find("#region_district")},this.regionEles.province.on("tap","li",$.proxy(this.onProvinceSelect,this)),this.regionEles.city.on("tap","li",$.proxy(this.onCitySelect,this)),this.regionEles.district.on("tap","li",$.proxy(this.onDistrict,this)))},_createProvince:function(e){var t=[];for(var n=0,r=this.regions.provinces.length;n<r;n+=1){var i=this.regions.provinces[n];t.push('<li data-name="'+i.provinceName+'"  data-value="'+i.provinceId+'" class="p_'+i.provinceId+"  "+(e===i.provinceId?"active":"")+'" >'+i.provinceName+"</li>")}return t.join("")},_createCity:function(e,t){var n=$.grep(this.regions.citys,function(t){return t.provinceId===e}),r=[];for(var i=0,s=n.length;i<s;i+=1){var o=n[i];r.push('<li  data-name="'+o.cityName+'"  data-value="'+o.cityId+'" data-provinceId="'+o.provinceId+'"  class="c_'+o.cityId+" "+(t===o.cityId?"active":"")+'" >'+o.cityName+"</li>")}return r.join("")},_createDistrict:function(e,t){var n=$.grep(this.regions.districts,function(t){return t.cityId===e}),r=[];for(var i=0,s=n.length;i<s;i+=1){var o=n[i];r.push('<li  data-name="'+o.districtName+'"  data-value="'+o.districtId+'" data-provinceId="'+o.cityId+'" class=" d_'+o.districtId+" "+(t===o.districtId?"active":"")+'"  >'+o.districtName+"</li>")}return r.join("")},create:function(e){this.onCreate(),this.opts=e,this.regionEles.province.html(""),this.regionEles.city.html(""),this.regionEles.district.html(""),this.regionEles.province.html(this._createProvince(e?e.provinceId:null)),e&&e.cityId&&e.provinceId&&(this.regionEles.city.html(this._createCity(e.provinceId,e.cityId)),this.regionEles.district.html(this._createDistrict(e.cityId,e.districtId)))},onProvinceSelect:function(e){var t=$(e.target);this.setSelectStyle(t,"p"),this.regionEles.city.html(this._createCity(t.data("value")))},setSelectStyle:function(e,t){t==="p"?(this.selectValue={},this.selectValue.province={name:e.data("name"),value:e.data("value")},this.regionEles.province.find("li").removeClass("active"),this.regionEles.province.find("li.p_"+e.data("value")).addClass("active")):t==="c"?(this.selectValue.city={name:e.data("name"),value:e.data("value")},this.selectValue.district={},this.regionEles.city.find("li").removeClass("active"),this.regionEles.city.find("li.c_"+e.data("value")).addClass("active")):(this.selectValue.district={name:e.data("name"),value:e.data("value")},this.regionEles.district.find("li").removeClass("active"),this.regionEles.district.find("li.d_"+e.data("value")).addClass("active"))},onCitySelect:function(e){var t=$(e.target);this.setSelectStyle(t,"c"),this.regionEles.district.html(this._createDistrict(t.data("value")))},onDistrict:function(e){var t=$(e.target);this.setSelectStyle(t,"d"),this.hide(),this.opts&&this.opts.callback&&this.opts.callback(this.selectValue)},show:function(e){this.create(e),$(document.body).pagecontainer("change",this.root,{role:"page",dataUrl:this.getWidgetId(),changeHash:!0})},hide:function(){$.mobile.back()}},s=new e.Class(t,i);return s.create=function(e){return this.instance||(r=e,this.instance=new this),this.instance},s});var mapObj,marker=new Array,windowsArr=new Array;define("MsWidgetMap",[],function(){"use strict";var e=!1,t,n={},r={init:function(e){if(!("AMap"in window&&"Map"in AMap))throw new Error("地图插件未加载成功");mapObj=new AMap.Map(e)},search:function(e){var t=this,n=e.container,r=e.container+"gaode_container",i=e.container+"gaode_img",s=e.container+"gaode_img_container",o=$("#"+e.container);o.empty();if(e.isStatic){var u=['<div id="'+r+'" style="width:0px;height:0px"></div>','<div id="'+s+'" style="width:100%;height:100%"><img id="'+i+'" /></div>'];o.append(u.join("")),this.init(r)}else this.init(e.container);var a;mapObj.plugin(["AMap.PlaceSearch"],function(){a=new AMap.PlaceSearch({}),AMap.event.addListener(a,"complete",$.proxy(function(r){this.searchCallback(r);var o=undefined;if(r&&r.poiList&&r.poiList.pois.length>0)for(var u=0,a=r.poiList.pois.length;u<a;u+=1){var f=r.poiList.pois[u];o=new AMap.LngLat(f.location.lng,f.location.lat);break}if(!o){$("#"+s).html("无法获取地址的图片信息，可能地址不详细或者不正确");return}if(e.isStatic&&o){var l=[];if(r&&r.poiList&&r.poiList.pois.length>0)for(var u=0,a=r.poiList.pois.length;u<a;u+=1){var f=r.poiList.pois[u];l.push(f.location.lng+","+f.location.lat)}$("#"+i).attr("src","http://restapi.amap.com/v3/staticmap?"+["key=19a22ce8d15eee958f3feb0932c2fbba","location="+o.lng+","+o.lat,"zoom=15","size="+$("#"+n).width()+"*"+$("#"+n).height(),"scale=1","markers=mid,0xFC6054,A:"+l.join(";")].join("&"))}var c;mapObj.plugin(["AMap.PlaceSearch"],function(){c=new AMap.PlaceSearch({type:"公交|地铁",pageSize:50,pageIndex:1}),AMap.event.addListener(c,"complete",$.proxy(function(n){var r=[];if(n&&n.poiList&&n.poiList.pois&&n.poiList.pois.length>0)for(var i=0,s=n.poiList.pois.length;i<s;i+=1){var o=n.poiList.pois[i];r.push(o.address)}e.callback&&e.callback(t.filter(r))},this)),c.searchNearBy("公交|地铁",o,1e3)})},t)),a.search(e.address)})},addmarker:function(e,t){var n=t.location.getLng(),r=t.location.getLat(),i={map:mapObj,icon:"http://webapi.amap.com/images/"+(e+1)+".png",position:new AMap.LngLat(n,r),topWhenClick:!0},s=new AMap.Marker(i);marker.push(new AMap.LngLat(n,r));var o=new AMap.InfoWindow({content:'<h3><font color="#00a6ac">  '+(e+1)+". "+t.name+"</font></h3>",size:new AMap.Size(300,0),autoMove:!0,offset:new AMap.Pixel(0,-20)});windowsArr.push(o);var u=function(e){o.open(mapObj,s.getPosition())};AMap.event.addListener(s,"click",u)},searchCallback:function(e){var t="",n=e.poiList.pois,r=n.length;for(var i=0;i<r;i++)t+="<div id='divid"+(i+1)+"' onmouseover='openMarkerTipById1("+i+",this)' onmouseout='onmouseout_MarkerStyle("+(i+1)+',this)\' style="font-size: 12px;cursor:pointer;padding:0px 0 4px 2px; border-bottom:1px solid #C1FFC1;"><table><tr><td><img src="http://webapi.amap.com/images/'+(i+1)+'.png"></td>'+'<td><h3><font color="#00a6ac">'+n[i].name+"</font></h3>",t+="</td></tr></table></div>",this.addmarker(i,n[i]);mapObj.setFitView()},filter:function(e){var t=[];for(var n=0,r=e.length;n<r;n+=1){var i=e[n].split(";");for(var s=0,o=i.length;s<o;s+=1)$.inArray(i[s],t)<0&&t.push(i[s])}return t},addGaodeScript:function(e){var t=document.getElementsByTagName("head")[0],n=document.createElement("script");n.type="text/javascript",n.onload=n.onreadystatechange=function(){if(!this.readyState||this.readyState==="loaded"||this.readyState==="complete")e(),n.onload=n.onreadystatechange=null},n.src="http://webapi.amap.com/maps?v=1.3&key=19a22ce8d15eee958f3feb0932c2fbba",n.id="gaodemap_js",t.appendChild(n)}};return r}),define("MsWidgetMenu",["MsCore","MsWidgetBase"],function(e,t){var n={__propertys__:function(){this.isCreated=!1,this.widgetType="MENU",this.root={},this.isDisplay=!1,this.currentClassName=""},initialize:function(e){this.opts=e},create:function(){var e=this;if(!this.isCreated){this.isCreated=!0;var t=['<ul class="pop_sort">'];for(var n=0,r=this.opts.menus.length;n<r;n+=1){var i=this.opts.menus[n];t.push('<li data-link="'+i.link+'" data-name="'+i.name+'">'+i.name+"</li>")}t.push("</ul>"),this.root=$(t.join("")),this.root.attr("id",this.getWidgetId()),$(document.body).append(this.root),this.root.on("animationend webkitAnimationEnd MSAnimationEnd oAnimationEnd",function(){e.root.removeClass("widget_menu_hide").removeClass("widget_menu_display"),e.isDisplay||e.root.css({display:"none"})}),this.opts.callback&&this.root.on("click","li",function(t){var n=$(t.target);e.toggleHide(),e.opts.callback({name:n.data("name"),link:n.data("link")})}),this.setStyle()}},toggle:function(){this.create();var e=this;this.isDisplay?(this.currentClassName="widget_menu_hide",this.isDisplay=!1):(this.currentClassName="widget_menu_display",this.isDisplay=!0,e.root.css({display:""})),this.root.addClass(this.currentClassName)},toggleHide:function(){this.isDisplay&&(this.currentClassName="widget_menu_hide",this.isDisplay=!1,this.root.addClass(this.currentClassName))},setStyle:function(){this.root.css({height:$(window).height()})}};return new e.Class(t,n)});