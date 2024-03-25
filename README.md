# Posts service
This microservice handles the operations related to the blogposts and comments.
They are:

1. Creating a blog post
2. Updating a blog post
3. Deleting a blog post
4. Getting a blog post by id
5. Getting a blog post by title
6. Getting blog posts by author name
7. Getting blog posts corresponding to a list of ids
8. Creating a comment on the blog post
9. Updating a comment on the blog post within 1 hour of its creation.
10.Deleting a comment on blog post
11.Getting comments for a given blog post
12.Upload a media for blog
13.Get media for a blog

## Data storage
The blogs, comments and the media files are stored in 3 different collections in MongoDB:
1. posts
2. comments
3. media

Since the media files in the blogs can be large, they would be uploaded by a separate call to the *uploadMediaByPostId* api. This API returns the id of the media record created. The front end should then call the *update* api with media reference added as part of content.

## Limitations
1. Currently the media is stored in a separate collection in MongoDB. If we want to store really large files, we should store them in GridFS or in a large setup, in a object store like S3. This was not part of initial requirements, and also not feasible within the timeframe and limited resources. However, the application is designed to handle this with minimal changes.
2. Currently, comments are not part of user engagement, but this can be achieved by passing the comments data periodically to the engagement service. 
3. Also, comments handling is not done as a separate microservice, but that can be done in future releases, making it possible to perform NLP operations to get user sentiments about the blog posts.
4. The current code relied on the front end to prevent the users from updating/ deleting the posts that they have not written.