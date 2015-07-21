var gulp = require('gulp'),
    jshint = require('gulp-jshint'),
    livereload = require('gulp-livereload');

var jsFiles = ['scripts/**/*.js', 'blades/**/*.js', 'Gulpfile.js'];

gulp.task('scripts', function() {
    return gulp.src(jsFiles)
        .pipe(jshint('.jshintrc'))
        .pipe(jshint.reporter('default'));
});

gulp.task('watch', function() {
    livereload.listen();

    gulp.watch(jsFiles, ['scripts']);

    gulp.watch(jsFiles).on('change', livereload.changed);
});

