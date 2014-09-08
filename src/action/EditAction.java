package action;

import com.opensymphony.xwork2.ActionSupport;
import common.Evn;
import common.PageList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import model.Article;
import model.Title;
import model.User;
import org.apache.struts2.ServletActionContext;
import service.ArticleService;

@SuppressWarnings("serial")
public class EditAction extends ActionSupport{
	int article_ID;
    
    /*edit*/
    String edit_title;
    String edit_category;
    String edit_text;
    String edit_image;
    /*add*/
    
    /*
     * list 获取当前用户后 判断权限 按照 state的值输出 列表中文章的类别
     * 初始值为已经发布的公告
     * 如果usr为真 则只输出用户自己的文章 a(all) u(author) e(editor)
     */
    PageList<Title> plist;
    int pageNo=1;
    char usr = 'a';
    //输入量
    Evn.ARTICLE state = Evn.ARTICLE.USE;
    String STATE = "ALL";
    String CATEGORY = "g";
    public String list(){
        HttpServletRequest request = ServletActionContext.getRequest();
        User user =(User)request.getSession().getAttribute("userInfo");
        if(user == null)
            return "list";
        //获取用户名
        List<Title> list = new ArticleService().getArticleList(usr, user.getUser_ID(), CATEGORY, this.getState(STATE));
        plist = new PageList<>(list,list.size(),30,pageNo,"hehe");
        return "list";
    }
    public String add(){
        HttpServletRequest request = ServletActionContext.getRequest();
        User user =(User)request.getSession().getAttribute("userInfo");
       
        request.getSession().setAttribute("article_ID",article_ID);
        if(user==null)
            return this.list();
        if(user.getUser_authority()=='u')
            return this.list();
        ArticleService as = new ArticleService();
        article_ID = as.addArticle(" "," "," ",user.getUser_ID(),Evn.ARTICLE_SAVE_LOCATION.DATABASE);
        title=text="待编辑";
        return "add";
    }
    public String edit(){
        HttpServletRequest request = ServletActionContext.getRequest();
        User user =(User)request.getSession().getAttribute("userInfo");
       
        request.getSession().setAttribute("article_ID",article_ID);
        if(user==null)
            return this.list();
        if(user.getUser_authority()=='u')
            return this.list();
        ArticleService as = new ArticleService();
        Article article = as.getAreticle(article_ID);
        title = article.getArticle_title();
        text = article.getArticle_text();
        category = article.getArticle_category();
        return "edit";
        
    }
    public String delete(){
        HttpServletRequest request = ServletActionContext.getRequest();
        User user =(User)request.getSession().getAttribute("userInfo");
        if(user==null)
            return this.list();
        if(user.getUser_authority()=='u')
            return this.list();
        new ArticleService().deleteArticle(article_ID);
        return list();
    }
    
    public String save(){
        HttpServletRequest request = ServletActionContext.getRequest();
        User user =(User)request.getSession().getAttribute("userInfo");
       
        request.getSession().removeAttribute("article_ID");
        if(user==null)
            return this.list();
        if(user.getUser_authority()=='u')
            return this.list();
        ArticleService as = new ArticleService();
        Article article = as.getAreticle(article_ID);
        article.setArticle_category(category);
        article.setArticle_title(title);
        article.setArticle_text(text);
        as.changleArticle(article, article.getArticle_ID(),user.getUser_ID());
        return this.list();
    }
    public String release(){
        HttpServletRequest request = ServletActionContext.getRequest();
        User user =(User)request.getSession().getAttribute("userInfo");
        if(user==null)
            return this.list();
        if(user.getUser_authority()=='u')
            return this.list();
        ArticleService as = new ArticleService();
        as.changleAricleState(article_ID,Evn.ARTICLE.USE);
        return this.list();
    }
    Evn.ARTICLE getState(String STATE){
    	switch(STATE.charAt(0))
    	{
    	case 'A':
    		return Evn.ARTICLE.ALL;
    	case 'U':
    		return Evn.ARTICLE.USE;
    	case 'E':
    		return Evn.ARTICLE.EDIT;
    	case 'D':
    		return Evn.ARTICLE.DELETE;
		default:
			return Evn.ARTICLE.ALL;
    	}
    	 
    } 
    public String getSTATE() {
		return STATE;
	}
	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}
	public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public PageList<Title> getPlist() {
        return plist;
    }

    public void setPlist(PageList<Title> plist) {
        this.plist = plist;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public Evn.ARTICLE getState() {
        return state;
    }

    public void setState(Evn.ARTICLE state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getArticle_ID() {
        return article_ID;
    }

    public void setArticle_ID(int article_ID) {
        this.article_ID = article_ID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
	public char getUsr() {
		return usr;
	}
	public void setUsr(char usr) {
		this.usr = usr;
	}
	public String getCATEGORY() {
		return CATEGORY;
	}
	public void setCATEGORY(String cATEGORY) {
		CATEGORY = cATEGORY;
	}
    
}
