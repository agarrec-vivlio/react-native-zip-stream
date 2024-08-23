Pod::Spec.new do |s|
  s.name         = 'react-native-zip-stream'
  s.version      = '1.0.0'
  s.summary      = 'Un module React Native pour streamer des fichiers ZIP.'
  s.description  = 'Ce module permet de lister et de streamer des fichiers à partir d\'une archive ZIP dans une application React Native.'
  s.homepage     = 'https://github.com/agarrec-vivlio/react-native-zip-stream'
  s.license      = { :type => 'MIT', :file => 'LICENSE' }
  s.author       = { 'Your Name' => 'your-email@example.com' }
  s.source       = { :git => 'https://github.com/agarrec-vivlio/react-native-zip-stream.git', :tag => s.version.to_s }
  s.source_files  = 'ios/**/*.{h,m,swift}'
  s.requires_arc = true
  s.dependency 'React'
  s.dependency 'ZipArchive'
end