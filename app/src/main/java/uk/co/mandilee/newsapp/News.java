package uk.co.mandilee.newsapp;

class News {
    private final String mTitle,
            mSection,
            mPublished,
            mUrl;

    News(String title, String section, String published, String url) {
        mTitle = title;
        mSection = section;
        mPublished = published;
        mUrl = url;
    }

    String getTitle() {
        return mTitle;
    }

    String getSection() {
        return mSection;
    }

    String getPublished() {
        return mPublished.length() > 0 ? mPublished : null;
    }

    String getUrl() {
        return mUrl.length() > 0 ? mUrl : null;
    }
}
