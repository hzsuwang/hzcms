/*
 * @example $('.lazy-load').imglazyload();
 * $('.lazy-load').imglazyload().on('error', function (e) {
 *     e.preventDefault();      //该图片不再加载
 * });
 */

(function(e){var t=[];e.fn.imglazyload=function(n){function c(e){var t=o?window:i.offset(),r=t[u.win[0]],s=t[u.win[1]];return r>=e[u.img[0]]-n.threshold-s&&r<=e[u.img[0]]+e[u.img[1]]}function h(r){var i=e(r),s={},o=i;f||(e.each(i.get(0).attributes,function(){~this.name.indexOf("data-")&&(s[this.name]=this.value)}),o=e("<img />").attr(s)),i.trigger("startload"),o.on("load",function(){!f&&i.replaceWith(o),i.trigger("loadcomplete"),o.off("load")}).on("error",function(){var n=e.Event("error");i.trigger(n),n.defaultPrevented||t.push(r),o.off("error").remove()}).attr("src",i.attr(n.urlName))}function p(){var n,i,s,o;for(n=t.length;n--;)i=e(o=t[n]),s=i.offset(),c(s)&&(r.call(t,n,1),h(o))}function d(){!f&&a&&e(t).append(a)}var r=Array.prototype.splice,n=e.extend({threshold:0,container:window,urlName:"data-url",placeHolder:"",eventName:"scrollStop",innerScroll:!1,isVertical:!0,loadDelay:0},n),i=e(n.container),s=n.isVertical,o=e.isWindow(i.get(0)),u={win:[s?"scrollY":"scrollX",s?"innerHeight":"innerWidth"],img:[s?"top":"left",s?"height":"width"]},a=e(n.placeHolder).length?e(n.placeHolder):null,f=e(this).is("img"),l=null;return!o&&(u.win=u.img),t=Array.prototype.slice.call(e(t.reverse()).add(this),0).reverse(),e.isFunction(e.fn.imglazyload.detect)?(d(),this):(e(document).ready(function(){d(),p()}),!n.innerScroll&&e(window).on(n.eventName+" ortchange",function(){n.loadDelay?(l&&(clearTimeout(l),l=null),l=setTimeout(p,n.loadDelay)):p()}),e.fn.imglazyload.detect=p,this)}})(Zepto);

var App = App || {};

App.initImgLazyload = function(){
	$('img[data-src]').imglazyload({
		 urlName : 'data-src',
		 refresh : true,
		loadDelay : 3e2
		 });
	};
	
	
var infoCtl = {                                                                
    get_local_param : function(param){                                           
        var reg = new RegExp("(^|&)"+ param+"=([^&]*)(&|$)");                    
        var r = window.location.search.substr(1).match(reg);                     
        if (r!=null)                                                             
            return unescape(r[2]);                                               
        return null;                                                             
    }                                                                           
} ;
	

var swipeLoad = (function ($) {
    var it = {},
        WH = $(window).height(),
        list, cfg, loadTips, pageCount, curPage = 1,
        loading = false,
        detectTimer = null,
        mainFrom = window.from,
		subFrom = infoCtl.get_local_param('subfrom'),
		fromParam = '',
		appData = null;

    var listTmp ;

    it.init = function (config) {
        if (!config) return;
        cfg = config;
        list = $(cfg.list);
        if (!list.length) return;
		
		listTmp = cfg.tpl;

        loadTips = $('#btmloader');
		
		loadTips.hide();

        it.bindAct();
		
		it.initData();
		
    };

    it.bindAct = function () {
        $(window).bind('scroll', it.detectLoad);
    };

    it.unbindAct = function () {
        $(window).unbind('scroll', it.detectLoad);
    };

    it.stopDetect = function () {
        it.unbindAct();
        loadTips.hide();
    };

    it.getScrollTop = function () {
        var t, doc = document;
        if (doc.documentElement && doc.documentElement.scrollTop) {
            t = doc.documentElement.scrollTop;
        } else if (doc.body) {
            t = doc.body.scrollTop;
        }
        return t;
    };

    it.belowthefold = function (element) {
        var fold = WH + it.getScrollTop();
        return fold <= $(element).offset().top - cfg.threshold;
    };

    it.reachBottom = function (fix) {
        var win = $(window),
            fix = fix || 0;

        if (navigator.userAgent.indexOf("iPhone") != -1) {
            fix += 70;
        };
        return (window.pageYOffset + win.height() + fix) >= document.body.scrollHeight;
    };
	
	it.initData = function () {
		
        if (loading) return;
		
		loadTips.show();
		
        loading = true;
        setTimeout(it.getInitData, cfg.loadDealy || 0)
       
    };

    it.detectLoad = function () {
		
        if (loading) return;
		
		loadTips.show();		
		
        var reachBottom = it.reachBottom(100);
		
        if (reachBottom) {
            loading = true;
            setTimeout(it.buildList, cfg.loadDealy || 0)
        };
    };
	
	it.getInitData = function () {
		var listUrl = cfg.url;
        $.ajax({
            type: 'GET',
            url:listUrl,
			success:function(data){		
			it.onGetData(data)
			}
        });

    };

    it.onGetData = function (data) {
		if ($.trim(data)=='') {
            it.stopDetect();
            return;
        };
		appData=data;
        it.buildList();
    };

    it.buildList = function () {
		var object = eval("("+appData+")");//转换为json对象 
       
	    var content="";
		for(var i=cfg.data.start;i<object.length&&i<cfg.data.start+cfg.data.count;i++){
		   content=content+'<div class="infoshow_02_pic" onclick="lookUser('+object[i].userId+','+object[i].gender+')">';
		   content=content+'<img src="/statics/images/loadimg.png" data-src="'+object[i].avatar+'" width="100%" height="100"/>';
		   content=content+'<div class="infoshow_02_paiming limit"><font>No.'+(i+16)+object[i].nickName+'</font></div>';
		   content=content+'</div>';
		}
		if(content==''){
		    it.stopDetect();
			loadTips.hide();
            return;
		}
		
		list.append(content);
		
		list.find('img').imglazyload();
		cfg.data.start += cfg.data.count;
        loading = false;
		loadTips.hide();
    };
    return it;
})($);


$(function(){
	App.initImgLazyload();
	window.swipeLoadCfg && swipeLoad.init(swipeLoadCfg);
	window.scroll(0,1);
});	
