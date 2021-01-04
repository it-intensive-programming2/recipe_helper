# coding: utf-8
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.inspection import inspect
from sqlalchemy.schema import FetchedValue
from sqlalchemy.orm import backref
from flask_security import Security, SQLAlchemyUserDatastore, UserMixin, RoleMixin
import json

db = SQLAlchemy()

# # Define models
# roles_users = db.Table(
#     "roles_users",
#     db.Column("user_id", db.Integer(), db.ForeignKey("web_user.id")),
#     db.Column("role_id", db.Integer(), db.ForeignKey("role.id")),
# )

# allergy: 계란, 우유, 밀, 갑각류, 생선, 호두, 돼지고기, 땅콩, 조개, 복숭아
# disease: 고혈압, 당뇨, 고지혈증 (0,1,2 = 없음, 보통, 심각)


class ClickHistory(db.Model):
    __tablename__ = "click_history"

    id = db.Column(db.Integer, primary_key=True)
    userID = db.Column(db.BigInteger)
    recipeID = db.Column(db.Integer)
    recipeClass = db.Column(db.Integer)


class Scrap(db.Model):
    __tablename__ = "scrap"

    id = db.Column(db.Integer, primary_key=True)
    recipeID = db.Column(db.Integer)
    userID = db.Column(db.BigInteger)
    recipeClass = db.Column(db.Integer)


class PostHeart(db.Model):
    __tablename__ = "post_heart"

    id = db.Column(db.Integer, primary_key=True)
    postID = db.Column(db.Integer)
    userID = db.Column(db.BigInteger)


class Ingredient(db.Model):
    __tablename__ = "ingredient"

    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255, "utf8_unicode_ci"))
    image = db.Column(db.String(255, "utf8_unicode_ci"))


class Recipe(db.Model):
    __tablename__ = "recipe"

    recipeID = db.Column(db.Integer, primary_key=True)
    className = db.Column(db.String(255, "utf8_unicode_ci"))
    classNum = db.Column(db.Integer)
    title = db.Column(db.String(500, "utf8_unicode_ci"))
    ingredientList = db.Column(db.String(500, "utf8_unicode_ci"))
    author = db.Column(db.String(225, "utf8_unicode_ci"))
    time = db.Column(db.String(225, "utf8_unicode_ci"))
    level = db.Column(db.String(225, "utf8_unicode_ci"))
    photo = db.Column(db.String(500, "utf8_unicode_ci"))


class RecipeUser(db.Model, UserMixin):
    __tablename__ = "recipe_user"

    userID = db.Column(db.BigInteger, primary_key=True)
    nickName = db.Column(db.String(100, "utf8_unicode_ci"))
    email = db.Column(db.String(100), unique=True)
    gender = db.Column(db.String(10))
    ageRange = db.Column(db.Integer)
    allergy = db.Column(db.String(20))
    disease = db.Column(db.String(20))
    profileUrl = db.Column(db.String(255))


class PostInfo(db.Model):
    __tablename__ = "post_info"

    postID = db.Column(db.Integer, primary_key=True)
    writerID = db.Column(
        db.ForeignKey("recipe_user.userID", ondelete="CASCADE"), index=True
    )
    content = db.Column(db.String(1000, "utf8_unicode_ci"))
    title = db.Column(db.String(255, "utf8_unicode_ci"))
    heart = db.Column(db.Integer)
    comment = db.Column(db.Integer)
    heartUser = db.Column(db.String(255))

    user = db.relationship(
        "RecipeUser", primaryjoin="RecipeUser.userID==PostInfo.writerID"
    )


class PostCommentInfo(db.Model):
    __tablename__ = "post_comment_info"

    id = db.Column(db.Integer, primary_key=True)
    postID = db.Column(
        db.ForeignKey("post_info.postID", ondelete="CASCADE"), index=True
    )
    writerID = db.Column(
        db.ForeignKey("recipe_user.userID", ondelete="CASCADE"), index=True
    )
    content = db.Column(db.String(255, "utf8_unicode_ci"))

    user = db.relationship(
        "RecipeUser", primaryjoin="RecipeUser.userID==PostCommentInfo.writerID"
    )


class Role(db.Model, RoleMixin):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(80), unique=True)
    description = db.Column(db.String(255))

    def __str__(self):
        return self.name


class RecipeClass(db.Model):
    __tablename__ = "recipe_class"

    userID = db.Column(db.BigInteger, primary_key=True)
    class_0 = db.Column(db.Integer, default=1)
    class_1 = db.Column(db.Integer, default=1)
    class_2 = db.Column(db.Integer, default=1)
    class_3 = db.Column(db.Integer, default=1)
    class_4 = db.Column(db.Integer, default=1)
    class_5 = db.Column(db.Integer, default=1)
    class_6 = db.Column(db.Integer, default=1)
    class_7 = db.Column(db.Integer, default=1)
    class_8 = db.Column(db.Integer, default=1)
    class_9 = db.Column(db.Integer, default=1)
    class_10 = db.Column(db.Integer, default=1)
    class_11 = db.Column(db.Integer, default=1)
    class_12 = db.Column(db.Integer, default=1)
    class_13 = db.Column(db.Integer, default=1)
    class_14 = db.Column(db.Integer, default=1)
    class_15 = db.Column(db.Integer, default=1)
    class_16 = db.Column(db.Integer, default=1)
    class_17 = db.Column(db.Integer, default=1)
    class_18 = db.Column(db.Integer, default=1)
    class_19 = db.Column(db.Integer, default=1)
    class_20 = db.Column(db.Integer, default=1)
    class_21 = db.Column(db.Integer, default=1)
    class_22 = db.Column(db.Integer, default=1)
    class_23 = db.Column(db.Integer, default=1)
    class_24 = db.Column(db.Integer, default=1)
    class_25 = db.Column(db.Integer, default=1)
    class_26 = db.Column(db.Integer, default=1)
    class_27 = db.Column(db.Integer, default=1)
    class_28 = db.Column(db.Integer, default=1)
    class_29 = db.Column(db.Integer, default=1)
    class_30 = db.Column(db.Integer, default=1)
    class_31 = db.Column(db.Integer, default=1)
    class_32 = db.Column(db.Integer, default=1)
    class_33 = db.Column(db.Integer, default=1)
    class_34 = db.Column(db.Integer, default=1)
    class_35 = db.Column(db.Integer, default=1)
    class_36 = db.Column(db.Integer, default=1)
    class_37 = db.Column(db.Integer, default=1)
    class_38 = db.Column(db.Integer, default=1)
    class_39 = db.Column(db.Integer, default=1)
    class_40 = db.Column(db.Integer, default=1)
    class_41 = db.Column(db.Integer, default=1)
    class_42 = db.Column(db.Integer, default=1)
    class_43 = db.Column(db.Integer, default=1)
    class_44 = db.Column(db.Integer, default=1)
    class_45 = db.Column(db.Integer, default=1)
    class_46 = db.Column(db.Integer, default=1)
    class_47 = db.Column(db.Integer, default=1)
    class_48 = db.Column(db.Integer, default=1)
    class_49 = db.Column(db.Integer, default=1)
    class_50 = db.Column(db.Integer, default=1)
    class_51 = db.Column(db.Integer, default=1)
    class_52 = db.Column(db.Integer, default=1)
    class_53 = db.Column(db.Integer, default=1)
    class_54 = db.Column(db.Integer, default=1)
    class_55 = db.Column(db.Integer, default=1)
    class_56 = db.Column(db.Integer, default=1)
    class_57 = db.Column(db.Integer, default=1)
    class_58 = db.Column(db.Integer, default=1)
    class_59 = db.Column(db.Integer, default=1)
    class_60 = db.Column(db.Integer, default=1)
    class_61 = db.Column(db.Integer, default=1)
    class_62 = db.Column(db.Integer, default=1)
    class_63 = db.Column(db.Integer, default=1)
    class_64 = db.Column(db.Integer, default=1)
    class_65 = db.Column(db.Integer, default=1)
    class_66 = db.Column(db.Integer, default=1)
    class_67 = db.Column(db.Integer, default=1)
    class_68 = db.Column(db.Integer, default=1)
    class_69 = db.Column(db.Integer, default=1)
    class_70 = db.Column(db.Integer, default=1)
    class_71 = db.Column(db.Integer, default=1)
    class_72 = db.Column(db.Integer, default=1)
    class_73 = db.Column(db.Integer, default=1)
    class_74 = db.Column(db.Integer, default=1)
    class_75 = db.Column(db.Integer, default=1)
    class_76 = db.Column(db.Integer, default=1)
    class_77 = db.Column(db.Integer, default=1)
    class_78 = db.Column(db.Integer, default=1)
    class_79 = db.Column(db.Integer, default=1)
    class_80 = db.Column(db.Integer, default=1)
    class_81 = db.Column(db.Integer, default=1)
    class_82 = db.Column(db.Integer, default=1)
    class_83 = db.Column(db.Integer, default=1)
    class_84 = db.Column(db.Integer, default=1)
    class_85 = db.Column(db.Integer, default=1)
    class_86 = db.Column(db.Integer, default=1)
    class_87 = db.Column(db.Integer, default=1)
    class_88 = db.Column(db.Integer, default=1)
    class_89 = db.Column(db.Integer, default=1)
    class_90 = db.Column(db.Integer, default=1)
    class_91 = db.Column(db.Integer, default=1)
    class_92 = db.Column(db.Integer, default=1)
    class_93 = db.Column(db.Integer, default=1)
    class_94 = db.Column(db.Integer, default=1)
    class_95 = db.Column(db.Integer, default=1)
    class_96 = db.Column(db.Integer, default=1)
    class_97 = db.Column(db.Integer, default=1)
    class_98 = db.Column(db.Integer, default=1)
    class_99 = db.Column(db.Integer, default=1)
    class_100 = db.Column(db.Integer, default=1)
    class_101 = db.Column(db.Integer, default=1)
    class_102 = db.Column(db.Integer, default=1)


# class WebUser(db.Model, UserMixin):
#     __tablename__ = "web_user"

#     id = db.Column(db.Integer, primary_key=True)
#     first_name = db.Column(db.String(255))
#     last_name = db.Column(db.String(255))
#     email = db.Column(db.String(255), unique=True)
#     password = db.Column(db.String(255))
#     active = db.Column(db.Boolean())
#     confirmed_at = db.Column(db.DateTime())
#     roles = db.relationship(
#         "Role", secondary=roles_users, backref=db.backref("web_users", lazy="dynamic")
#     )

#     def __str__(self):
#         return self.email


# class Serializer(object):
#     def serialize(self):
#         return {c: getattr(self, c) for c in inspect(self).attrs.keys()}

#     @staticmethod
#     def serialize_list(l):
#         return [m.serialize() for m in l]


# class SignOutUser(db.Model):
#     __tablename__ = "signout_user"

#     userID = db.Column(db.Integer, primary_key=True)
#     writtenTime = db.Column(db.DateTime)


# class CommunityAll(db.Model):
#     __tablename__ = "community_all"

#     communityID = db.Column(db.Integer, primary_key=True)
#     communityName = db.Column(db.String(20, "utf8_unicode_ci"))


# class LikeToAll(db.Model):
#     __tablename__ = "like_to_all"

#     likeID = db.Column(db.Integer, primary_key=True)
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     articleID = db.Column(
#         db.ForeignKey("article_all.articleID", ondelete="CASCADE"), index=True
#     )

#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="LikeToAll.userID == UserInfo.userID",
#         backref=backref("like_to_alls", cascade="all,delete"),
#     )
#     article = db.relationship(
#         "ArticleAll",
#         primaryjoin="LikeToAll.articleID == ArticleAll.articleID",
#         backref=backref("like_to_alls", cascade="all,delete"),
#     )


# class LikeToSchool(db.Model):
#     __tablename__ = "like_to_school"

#     likeID = db.Column(db.Integer, primary_key=True)
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     articleID = db.Column(
#         db.ForeignKey("article_school.articleID", ondelete="CASCADE"), index=True
#     )

#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="LikeToSchool.userID == UserInfo.userID",
#         backref=backref("like_to_schools", cascade="all,delete"),
#     )
#     article = db.relationship(
#         "ArticleSchool",
#         primaryjoin="LikeToSchool.articleID == ArticleSchool.articleID",
#         backref=backref("like_to_schools", cascade="all,delete"),
#     )


# class LikeToRegion(db.Model):
#     __tablename__ = "like_to_region"

#     likeID = db.Column(db.Integer, primary_key=True)
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     articleID = db.Column(
#         db.ForeignKey("article_region.articleID", ondelete="CASCADE"), index=True
#     )

#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="LikeToRegion.userID == UserInfo.userID",
#         backref=backref("like_to_regions", cascade="all,delete"),
#     )
#     article = db.relationship(
#         "ArticleRegion",
#         primaryjoin="LikeToRegion.articleID == ArticleRegion.articleID",
#         backref=backref("like_to_regions", cascade="all,delete"),
#     )


# class CommunityRegion(db.Model):
#     __tablename__ = "community_region"

#     communityID = db.Column(db.Integer, primary_key=True)
#     regionID = db.Column(
#         db.ForeignKey("region_info.regionID", ondelete="CASCADE"), index=True
#     )
#     communityName = db.Column(db.String(20, "utf8_unicode_ci"))

#     region_info = db.relationship(
#         "RegionInfo",
#         primaryjoin="CommunityRegion.regionID == RegionInfo.regionID",
#         backref=backref("community_regions", cascade="all,delete"),
#     )


# class CommunitySchool(db.Model):
#     __tablename__ = "community_school"

#     communityID = db.Column(db.Integer, primary_key=True)
#     schoolID = db.Column(
#         db.ForeignKey("school_info.schoolID", ondelete="CASCADE"),
#         nullable=False,
#         index=True,
#     )
#     communityName = db.Column(db.String(20, "utf8_unicode_ci"))

#     school_info = db.relationship(
#         "SchoolInfo",
#         primaryjoin="CommunitySchool.schoolID == SchoolInfo.schoolID",
#         backref=backref("community_schools", cascade="all,delete"),
#     )


# class ArticleReport(db.Model):
#     __tablename__ = "article_report"

#     reportID = db.Column(db.Integer, primary_key=True)
#     articleID = db.Column(db.Integer)
#     communityID = db.Column(db.Integer)
#     articleType = db.Column(db.String(50, "utf8_unicode_ci"))
#     userID = db.Column(db.Integer)
#     title = db.Column(db.String(50, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     reportNum = db.Column(db.Integer)
#     reportUser = db.Column(db.String(50, "utf8_unicode_ci"))


# class LiveShow(db.Model):
#     __tablename__ = "live_show"

#     liveShowID = db.Column(db.Integer, primary_key=True)
#     univID = db.Column(
#         db.ForeignKey("univ_info.univID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     univTitle = db.Column(db.String(50, "utf8_unicode_ci"))
#     major = db.Column(db.String(50, "utf8_unicode_ci"))
#     title = db.Column(db.String(50, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     heart = db.Column(db.Integer, server_default=FetchedValue())
#     writtenTime = db.Column(db.DateTime)

#     univ_info = db.relationship(
#         "UnivInfo",
#         primaryjoin="UnivInfo.univID == LiveShow.univID",
#         backref=backref("live_shows", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="LiveShow.userID == UserInfo.userID",
#         backref=backref("live_shows", cascade="all,delete"),
#     )


# class ArticleAll(db.Model):
#     __tablename__ = "article_all"

#     articleID = db.Column(db.Integer, primary_key=True)
#     communityID = db.Column(
#         db.ForeignKey("community_all.communityID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"), nullable=False)
#     title = db.Column(db.String(50, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     viewNumber = db.Column(db.Integer, server_default=FetchedValue())
#     reply = db.Column(db.Integer, server_default=FetchedValue())
#     heart = db.Column(db.Integer, server_default=FetchedValue())
#     writtenTime = db.Column(db.DateTime)

#     community = db.relationship(
#         "CommunityAll",
#         primaryjoin="ArticleAll.communityID == CommunityAll.communityID",
#         backref=backref("article_alls", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ArticleAll.userID == UserInfo.userID",
#         backref=backref("article_alls", cascade="all,delete"),
#     )


# class ArticleRegion(db.Model):
#     __tablename__ = "article_region"

#     articleID = db.Column(db.Integer, primary_key=True)
#     communityID = db.Column(
#         db.ForeignKey("community_region.communityID", ondelete="CASCADE"), index=True
#     )
#     regionID = db.Column(
#         db.ForeignKey("region_info.regionID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"), nullable=False)
#     title = db.Column(db.String(50, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     viewNumber = db.Column(db.Integer, server_default=FetchedValue())
#     reply = db.Column(db.Integer, server_default=FetchedValue())
#     heart = db.Column(db.Integer, server_default=FetchedValue())
#     writtenTime = db.Column(db.DateTime)

#     community = db.relationship(
#         "CommunityRegion",
#         primaryjoin="ArticleRegion.communityID == CommunityRegion.communityID",
#         backref=backref("article_regions", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ArticleRegion.userID == UserInfo.userID",
#         backref=backref("article_regions", cascade="all,delete"),
#     )
#     region_info = db.relationship(
#         "RegionInfo",
#         primaryjoin="ArticleRegion.regionID == RegionInfo.regionID",
#         backref=backref("article_regions", cascade="all,delete"),
#     )


# class ArticleSchool(db.Model):
#     __tablename__ = "article_school"

#     articleID = db.Column(db.Integer, primary_key=True)
#     communityID = db.Column(
#         db.ForeignKey("community_school.communityID", ondelete="CASCADE"), index=True
#     )
#     schoolID = db.Column(
#         db.ForeignKey("school_info.schoolID", ondelete="CASCADE"),
#         nullable=False,
#         index=True,
#     )
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"), nullable=False)
#     title = db.Column(db.String(50, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     viewNumber = db.Column(db.Integer, server_default=FetchedValue())
#     reply = db.Column(db.Integer, server_default=FetchedValue())
#     heart = db.Column(db.Integer, server_default=FetchedValue())
#     writtenTime = db.Column(db.DateTime)

#     community = db.relationship(
#         "CommunitySchool",
#         primaryjoin="ArticleSchool.communityID == CommunitySchool.communityID",
#         backref=backref("articles", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ArticleSchool.userID == UserInfo.userID",
#         backref=backref("article_schools", cascade="all,delete"),
#     )
#     school_info = db.relationship(
#         "SchoolInfo",
#         primaryjoin="ArticleSchool.schoolID == SchoolInfo.schoolID",
#         backref=backref("user_info_schools", cascade="all,delete"),
#     )


# class RegionInfo(db.Model):
#     __tablename__ = "region_info"

#     regionID = db.Column(db.Integer, primary_key=True)
#     regionName = db.Column(db.String(20, "utf8_unicode_ci"))


# class SchoolInfo(db.Model):
#     __tablename__ = "school_info"

#     schoolID = db.Column(db.Integer, primary_key=True)
#     studentNum = db.Column(db.Integer)
#     regionID = db.Column(
#         db.ForeignKey("region_info.regionID", ondelete="CASCADE"), index=True
#     )
#     regionName = db.Column(db.String(100, "utf8_unicode_ci"))
#     townName = db.Column(db.String(100, "utf8_unicode_ci"))
#     schoolName = db.Column(db.String(1000, "utf8_unicode_ci"))
#     gender = db.Column(db.Integer)
#     contact = db.Column(db.String(20, "utf8_unicode_ci"))
#     homePage = db.Column(db.String(1000, "utf8_unicode_ci"))

#     region_info = db.relationship(
#         "RegionInfo",
#         primaryjoin="SchoolInfo.regionID == RegionInfo.regionID",
#         backref=backref("school_infos", cascade="all,delete"),
#     )


# class UnivInfo(db.Model):
#     __tablename__ = "univ_info"

#     univID = db.Column(db.Integer, primary_key=True)
#     univName = db.Column(db.String(100, "utf8_unicode_ci"))
#     subRegion = db.Column(db.String(100, "utf8_unicode_ci"))
#     homePage = db.Column(db.String(1000, "utf8_unicode_ci"))
#     eduHomePage = db.Column(db.String(300, "utf8_unicode_ci"))
#     logoPossible = db.Column(db.Integer)


# class CafeteriaInfo(db.Model):
#     __tablename__ = "cafeteria_info"

#     schoolID = db.Column(db.Integer, primary_key=True)
#     regionID = db.Column(
#         db.ForeignKey("region_info.regionID", ondelete="CASCADE"), index=True
#     )
#     version = db.Column(db.DateTime)
#     curCafeMenu = db.Column(db.String(7000, "utf8_unicode_ci"))
#     nextCafeMenu = db.Column(db.String(7000, "utf8_unicode_ci"))


# class ContestInfo(db.Model):
#     __tablename__ = "contest_info"

#     contestID = db.Column(db.Integer, primary_key=True)
#     title = db.Column(db.String(100, "utf8_unicode_ci"))
#     imageUrl = db.Column(db.String(100, "utf8_unicode_ci"))
#     content = db.Column(db.String(7000, "utf8_unicode_ci"))
#     area = db.Column(db.String(100, "utf8_unicode_ci"))
#     sponsor = db.Column(db.String(100, "utf8_unicode_ci"))
#     start = db.Column(db.String(100, "utf8_unicode_ci"))
#     end = db.Column(db.String(100, "utf8_unicode_ci"))
#     prize = db.Column(db.String(100, "utf8_unicode_ci"))
#     firstPrize = db.Column(db.String(100, "utf8_unicode_ci"))
#     homePage = db.Column(db.String(100, "utf8_unicode_ci"))


# class UserCredential(db.Model):
#     __tablename__ = "user_credential"

#     userID = db.Column(db.Integer, primary_key=True)
#     pwd = db.Column(db.String(20, "utf8_unicode_ci"))


# class UserInfo(db.Model):
#     __tablename__ = "user_info"

#     userID = db.Column(db.Integer, primary_key=True)
#     schoolID = db.Column(
#         db.ForeignKey("school_info.schoolID", ondelete="CASCADE"),
#         nullable=False,
#         index=True,
#     )
#     schoolName = db.Column(db.String(100, "utf8_unicode_ci"), nullable=False)
#     regionName = db.Column(db.String(100, "utf8_unicode_ci"), nullable=False)
#     studentName = db.Column(db.String(100, "utf8_unicode_ci"), nullable=False)
#     authorized = db.Column(db.Integer)
#     signupDate = db.Column(db.DateTime)
#     regionID = db.Column(
#         db.ForeignKey("region_info.regionID", ondelete="CASCADE"), index=True
#     )
#     email = db.Column(db.String(100, "utf8_unicode_ci"), nullable=False)
#     age = db.Column(db.Integer, nullable=False)
#     gender = db.Column(db.Integer, nullable=False)
#     grade = db.Column(db.Integer, nullable=False)
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"), nullable=False)
#     banned = db.Column(db.Integer)
#     fcmToken = db.Column(db.String(200, "utf8_unicode_ci"), nullable=False)

#     school_info = db.relationship(
#         "SchoolInfo",
#         primaryjoin="UserInfo.schoolID == SchoolInfo.schoolID",
#         backref=backref("user_infos", cascade="all,delete"),
#     )
#     region_info = db.relationship(
#         "RegionInfo",
#         primaryjoin="UserInfo.regionID == RegionInfo.regionID",
#         backref=backref("user_infos", cascade="all,delete"),
#     )


# class ReplyAll(db.Model):
#     __tablename__ = "reply_all"

#     replyID = db.Column(db.Integer, primary_key=True)
#     articleID = db.Column(
#         db.ForeignKey("article_all.articleID", ondelete="CASCADE"), index=True
#     )
#     communityID = db.Column(
#         db.ForeignKey("community_all.communityID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     writtenTime = db.Column(db.DateTime)

#     article = db.relationship(
#         "ArticleAll",
#         primaryjoin="ReplyAll.articleID == ArticleAll.articleID",
#         backref=backref("reply_alls", cascade="all,delete"),
#     )
#     community = db.relationship(
#         "CommunityAll",
#         primaryjoin="ReplyAll.communityID == CommunityAll.communityID",
#         backref=backref("reply_alls", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ReplyAll.userID == UserInfo.userID",
#         backref=backref("reply_alls", cascade="all,delete"),
#     )


# class ReplyRegion(db.Model):
#     __tablename__ = "reply_region"

#     replyID = db.Column(db.Integer, primary_key=True)
#     articleID = db.Column(
#         db.ForeignKey("article_region.articleID", ondelete="CASCADE"), index=True
#     )
#     communityID = db.Column(
#         db.ForeignKey("community_region.communityID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     writtenTime = db.Column(db.DateTime)

#     article = db.relationship(
#         "ArticleRegion",
#         primaryjoin="ReplyRegion.articleID == ArticleRegion.articleID",
#         backref=backref("reply_regions", cascade="all,delete"),
#     )
#     community = db.relationship(
#         "CommunityRegion",
#         primaryjoin="ReplyRegion.communityID == CommunityRegion.communityID",
#         backref=backref("reply_regions", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ReplyRegion.userID == UserInfo.userID",
#         backref=backref("reply_regions", cascade="all,delete"),
#     )


# class ReplySchool(db.Model):
#     __tablename__ = "reply_school"

#     replyID = db.Column(db.Integer, primary_key=True)
#     articleID = db.Column(
#         db.ForeignKey("article_school.articleID", ondelete="CASCADE"), index=True
#     )
#     communityID = db.Column(
#         db.ForeignKey("community_school.communityID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(
#         db.ForeignKey("user_info.userID", ondelete="CASCADE"), index=True
#     )
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     writtenTime = db.Column(db.DateTime)

#     article = db.relationship(
#         "ArticleSchool",
#         primaryjoin="ReplySchool.articleID == ArticleSchool.articleID",
#         backref=backref("reply_schools", cascade="all,delete"),
#     )
#     community = db.relationship(
#         "CommunitySchool",
#         primaryjoin="ReplySchool.communityID == CommunitySchool.communityID",
#         backref=backref("reply_schools", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ReplySchool.userID == UserInfo.userID",
#         backref=backref("reply_schools", cascade="all,delete"),
#     )


# class ReReplySchool(db.Model):
#     __tablename__ = "rereply_school"

#     replyID = db.Column(db.Integer, primary_key=True)
#     parentReplyID = db.Column(
#         db.ForeignKey("reply_school.replyID", ondelete="CASCADE"), index=True
#     )
#     articleID = db.Column(
#         db.ForeignKey("article_school.articleID", ondelete="CASCADE"), index=True
#     )
#     communityID = db.Column(
#         db.ForeignKey("community_school.communityID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(db.ForeignKey("user_info.userID"), index=True)
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     writtenTime = db.Column(db.DateTime)

#     article = db.relationship(
#         "ArticleSchool",
#         primaryjoin="ReReplySchool.articleID == ArticleSchool.articleID",
#         backref=backref("rereplie_schools", cascade="all,delete"),
#     )
#     community = db.relationship(
#         "CommunitySchool",
#         primaryjoin="ReReplySchool.communityID == CommunitySchool.communityID",
#         backref=backref("rereplie_schools", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ReReplySchool.userID == UserInfo.userID",
#         backref=backref("rereplie_schools", cascade="all,delete"),
#     )
#     reply = db.relationship(
#         "ReplySchool",
#         primaryjoin="ReReplySchool.parentReplyID == ReplySchool.replyID",
#         backref=backref("rereplie_schools", cascade="all,delete"),
#     )


# class ReReplyRegion(db.Model):
#     __tablename__ = "rereply_region"

#     replyID = db.Column(db.Integer, primary_key=True)
#     parentReplyID = db.Column(
#         db.ForeignKey("reply_region.replyID", ondelete="CASCADE"), index=True
#     )
#     articleID = db.Column(
#         db.ForeignKey("article_region.articleID", ondelete="CASCADE"), index=True
#     )
#     communityID = db.Column(
#         db.ForeignKey("community_region.communityID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(db.ForeignKey("user_info.userID"), index=True)
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     writtenTime = db.Column(db.DateTime)

#     article = db.relationship(
#         "ArticleRegion",
#         primaryjoin="ReReplyRegion.articleID == ArticleRegion.articleID",
#         backref=backref("rereplie_regions", cascade="all,delete"),
#     )
#     community = db.relationship(
#         "CommunityRegion",
#         primaryjoin="ReReplyRegion.communityID == CommunityRegion.communityID",
#         backref=backref("rereplie_regions", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ReReplyRegion.userID == UserInfo.userID",
#         backref=backref("rereplie_regions", cascade="all,delete"),
#     )
#     reply = db.relationship(
#         "ReplyRegion",
#         primaryjoin="ReReplyRegion.parentReplyID == ReplyRegion.replyID",
#         backref=backref("rereplie_regions", cascade="all,delete"),
#     )


# class ReReplyAll(db.Model):
#     __tablename__ = "rereply_all"

#     replyID = db.Column(db.Integer, primary_key=True)
#     parentReplyID = db.Column(
#         db.ForeignKey("reply_all.replyID", ondelete="CASCADE"), index=True
#     )
#     articleID = db.Column(
#         db.ForeignKey("article_all.articleID", ondelete="CASCADE"), index=True
#     )
#     communityID = db.Column(
#         db.ForeignKey("community_all.communityID", ondelete="CASCADE"), index=True
#     )
#     userID = db.Column(db.ForeignKey("user_info.userID"), index=True)
#     nickName = db.Column(db.String(20, "utf8_unicode_ci"))
#     content = db.Column(db.String(5000, "utf8_unicode_ci"))
#     writtenTime = db.Column(db.DateTime)

#     article = db.relationship(
#         "ArticleAll",
#         primaryjoin="ReReplyAll.articleID == ArticleAll.articleID",
#         backref=backref("rereplie_alls", cascade="all,delete"),
#     )
#     community = db.relationship(
#         "CommunityAll",
#         primaryjoin="ReReplyAll.communityID == CommunityAll.communityID",
#         backref=backref("rereplie_alls", cascade="all,delete"),
#     )
#     user_info = db.relationship(
#         "UserInfo",
#         primaryjoin="ReReplyAll.userID == UserInfo.userID",
#         backref=backref("rereplie_alls", cascade="all,delete"),
#     )
#     reply = db.relationship(
#         "ReplyAll",
#         primaryjoin="ReReplyAll.parentReplyID == ReplyAll.replyID",
#         backref=backref("rereplie_alls", cascade="all,delete"),
#     )
