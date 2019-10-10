package pulkit.com.torontowastewizard.Model

import java.io.Serializable

class Waste : Serializable {
    var body: String? = null
    var category: String? = null
    var title: String? = null
    var keywords: String? = null

    constructor(body: String, category: String, title: String, keywords: String) {
        this.body = body
        this.category = category
        this.title = title
        this.keywords = keywords
    }

    constructor() {

    }
}
