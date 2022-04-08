///API接口统一管理类
class ApiUrls {

  static const String baseUrl = 'http://192.168.252.1';
  //static const String baseUrl = 'http://192.168.3.13';

  ///首页获取banner数据
  static const String homeBanner = "/promotion/api/banner/app/index";

  ///首页获取推荐商品列表
  static const String homeRecommendList = "/promotion/api/recommend/product/list";

  ///首页获取banner数据
  static const String columnDetailById = "/promotion/api/column/detail";

  ///获取分类树
  static const String categoryTree = "/product/api/category/list/tree";


  static const String spuDetail = "/product/api/spu/detail/";

  static const String memberLogin = "/member/api/member/login";
  static const String memberPasswordRegister = "/member/api/member/phone/register";
  static const String memberInfo = "/member/api/member/detail";

}